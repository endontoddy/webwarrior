package webwarrior

import cats.free.Free
import webwarrior.interpreters.Interpreter.DSL._
import webwarrior.interpreters.Interpreter.InterpreterAction

class ProgramBuilder {
  def apply(page: Page): (Unit => Free[InterpreterAction, Unit]) = { _ =>
    for {
      _ <- displayText(page.description)
      _ <- processFinalAction(page.finalAction)
    } yield ()
  }

  private def processFinalAction(action: FinalPageAction) = action match {
    case FinalPageAction.Choices(cs) => displayOptions(cs)
  }

}
