package object webwarrior {
  type PageId = String

  sealed trait PageAction
  object PageAction {
    case class DisplayText(text: String) extends PageAction
  }

  sealed trait FinalPageAction
  object FinalPageAction {
    case class Choices(choices: List[Choice]) extends FinalPageAction
    case object End extends FinalPageAction
  }

  case class Page(description: String, finalAction: FinalPageAction, actions: List[PageAction] = Nil)

  case class Book(pages: Map[PageId, Page], initialPage: String)

  case class Choice(desc: String, pid: PageId)
}
