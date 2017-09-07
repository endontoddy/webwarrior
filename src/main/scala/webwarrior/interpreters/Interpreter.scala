package webwarrior.interpreters

import cats.{Id, ~>}
import cats.free.Free
import webwarrior.Choice
import webwarrior.interpreters.Interpreter.InterpreterAction
import webwarrior.interpreters.Interpreter.InterpreterAction.{Choices, Text}

trait Interpreter extends (InterpreterAction ~> Id)

object Interpreter {
  sealed trait InterpreterAction[T]
  object InterpreterAction {
    case class Text(t: String) extends InterpreterAction[Unit]
    case class Choices(os: List[Choice]) extends InterpreterAction[Unit]
  }

  object DSL {
    def displayText(t: String): Free[InterpreterAction, Unit] = Free.liftF(Text(t))
    def displayOptions(os: List[Choice]): Free[InterpreterAction, Unit] = Free.liftF(Choices(os))
  }
}
