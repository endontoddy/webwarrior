package object webwarrior {
  type PageId = String

  sealed trait PageAction
  object PageAction {
    case class DisplayText(text: String) extends PageAction
    case class Damage(d: Int) extends PageAction
  }

  case class Enemy(name: String, skill: Int, stamina: Int)

  sealed trait FinalPageAction
  object FinalPageAction {
    case class Choices(choices: List[Choice]) extends FinalPageAction
    case object End extends FinalPageAction
    case class TestLuck(success: Choice, failure: Choice) extends FinalPageAction
    case class Combat(enemies: List[Enemy], success: Choice) extends FinalPageAction
  }

  case class Page(description: String, finalAction: FinalPageAction, actions: List[PageAction] = Nil)

  case class Book(pages: Map[PageId, Page], initialPage: String)

  case class Choice(desc: String, pid: PageId)

  case class Adventurer(
                       initialSkill: Int,
                       skill: Int,
                       initialStamina: Int,
                       stamina: Int,
                       initialLuck: Int,
                       luck: Int
                       )

  object Adventurer {
    def apply(skill: Int, stamina: Int, luck: Int): Adventurer =
      Adventurer(skill, skill, stamina, stamina, luck, luck)
  }
}
