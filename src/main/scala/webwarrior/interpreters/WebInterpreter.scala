package webwarrior.interpreters

import cats.Id
import org.scalajs.jquery.jQuery
import webwarrior.interpreters.Interpreter.InterpreterAction
import webwarrior.interpreters.Interpreter.InterpreterAction._

class WebInterpreter extends Interpreter {
  override def apply[A](fa: InterpreterAction[A]): Id[A] = fa match {
    case Text(t)            => jQuery("#text").append(s"<p>$t</p>"): Unit
    case Choices(os) =>
      jQuery("#options").empty()
      jQuery("#options").append(s"<ul>${
        os.map(o => s"""<li><a href="${o.pid}">${o.desc}</a></li>""").mkString("\n")
      }</ul>"): Unit
  }
}
