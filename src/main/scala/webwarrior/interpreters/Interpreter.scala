package webwarrior.interpreters

import cats.free.Free
import webwarrior._
trait Interpreter {
  def apply(program: (Adventurer) => Free[Interpreter.InterpreterAction, Unit])
                  (implicit book: Book, adventurer: Adventurer): Unit

  def initialise(adventurer: Adventurer, book: Book): Unit
}

object Interpreter {

  case class Link(desc: String, callback: Adventurer => Unit)

  sealed trait InterpreterAction[T]
  object InterpreterAction {
    case class Text(t: String) extends InterpreterAction[Unit]
    case class Choices(os: List[Link]) extends InterpreterAction[Unit]
    case class UpdateCharacterSheet(a: Adventurer) extends InterpreterAction[Unit]
    case class DisplayEnemies(enemies: List[Enemy]) extends InterpreterAction[Unit]
  }

  object DSL {
    import InterpreterAction._
    def displayText(t: String): Free[InterpreterAction, Unit] = Free.liftF(Text(t))
    def displayOptions(os: List[Link]): Free[InterpreterAction, Unit] = Free.liftF(Choices(os))
    def updateCharacterSheet(a: Adventurer): Free[InterpreterAction, Unit] = Free.liftF(UpdateCharacterSheet(a))
    def displayEnemies(es: List[Enemy]): Free[InterpreterAction, Unit] = Free.liftF(DisplayEnemies(es))
  }
}
