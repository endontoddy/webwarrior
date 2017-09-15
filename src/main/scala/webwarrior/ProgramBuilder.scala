package webwarrior

import cats.free.Free
import webwarrior.FinalPageAction.Combat
import webwarrior.interpreters.Interpreter.DSL._
import webwarrior.interpreters.Interpreter.{InterpreterAction, Link}

class ProgramBuilder(diceRoller: DiceRoll) {
  def apply(page: Page)
           (
             pidLoader: (PageId, Book, Adventurer) => Unit,
             combatLoader: (Combat, Book, Adventurer) => Unit
           )
           (implicit b: Book): (Adventurer => Free[InterpreterAction, Unit]) = { a: Adventurer =>
    for {
      _   <- displayText(page.description)
      a2  <- page.actions
              .foldLeft(Free.pure[InterpreterAction, Adventurer](a))( (all, pa) =>
                all.flatMap(processPageAction(pa, _))
              )
      a3  <- processFinalAction(page.finalAction, a2, pidLoader, combatLoader)
      _   <- updateCharacterSheet(a3)
    } yield ()
  }

  private def processPageAction(action: PageAction, a: Adventurer): Free[InterpreterAction, Adventurer] = action match {
    case PageAction.Damage(d) => displayText(s"You have taken $d damage").map(_ => a.copy(stamina = a.stamina - d))
    case PageAction.DisplayText(t) => displayText(t).map(_ => a)
  }

  private def processFinalAction(
                                  action: FinalPageAction,
                                  a: Adventurer,
                                  pidLoader: (PageId, Book, Adventurer) => Unit,
                                  combatLoader: (Combat, Book, Adventurer) => Unit
                                )
                                (implicit b: Book) = action match {
    case FinalPageAction.Choices(cs)    => displayOptions(cs.map(toLink(_, pidLoader))).map(_ => a)
    case FinalPageAction.End            => displayOptions(Nil).map(_ => a)
    case FinalPageAction.TestLuck(s, f) =>
      for {
        rolls     <- rollDice(2)
        succeeded = rolls.sum < a.luck
        _         <- displayText(s"Testing luck... ${rolls.mkString(", ")} ${if(succeeded) "" else "UN"}LUCKY!")
        _         <- displayOptions( toLink(if (succeeded) s else f, pidLoader) :: Nil )
      } yield if(succeeded) a.copy(luck = a.luck -1) else a
    case c: FinalPageAction.Combat => buildCombat(c, a, combatLoader, fightRound = false)
  }

  private def buildCombat(
                           combat: Combat,
                           a: Adventurer,
                           combatLoader: (Combat, Book, Adventurer) => Unit,
                           fightRound: Boolean = true
                         )
                         (implicit b: Book): Free[InterpreterAction, Adventurer] =
    for {
//      (a2, es)
      r  <- combat.enemies
                    .foldLeft(Free.pure[InterpreterAction, (Adventurer, List[Enemy])](a, List.empty[Enemy])) ((res, e) =>
                      res.map { case (ad, es) =>
                        val (newA, newE) = if (fightRound) fight(ad, e) else (a, e)
                        (newA, newE :: es)
                      }
                    )
      _         <- displayEnemies(r._2)
      _         <- displayOptions(Link("Fight!", a3 => combatLoader(Combat(r._2, combat.success), b, a3)) :: Nil)
    } yield r._1

  private def fight(adventurer: Adventurer, enemy: Enemy): (Adventurer, Enemy) = {
    val adRoll = adventurer.skill + diceRoller(2).sum
    val enRoll = enemy.skill + diceRoller(2).sum
    ???
  }


  def combat(combat: Combat, combatLoader: (Combat, Book, Adventurer) => Unit)
            (implicit b: Book): Adventurer => Free[InterpreterAction, Unit] =
    a => buildCombat(combat, a, combatLoader).map(_ => ())

  def error(msg: String)(implicit b: Book): (Adventurer) => Free[InterpreterAction, Unit] = a =>
    for {
      _ <- displayText("An error occurred: " + msg)
      _ <- processFinalAction(FinalPageAction.End, a, (_,_,_) => (), (_,_,_) => ())
    } yield ()

  private def rollDice(numberOfDice: Int = 1, sides: Int = 6): Free[InterpreterAction, List[Int]] =
    Free.pure(diceRoller(numberOfDice, sides))


  private def toLink(c: Choice, pidLoader: (PageId, Book, Adventurer) => Unit)(implicit b: Book): Link =
    Link(c.desc, a => pidLoader(c.pid, b, a))
}