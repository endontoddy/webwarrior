package webwarrior.interpreters

import cats.Id
import org.scalajs.jquery.jQuery
import webwarrior.{Book, PageId}
import webwarrior.interpreters.Interpreter.InterpreterAction
import webwarrior.interpreters.Interpreter.InterpreterAction._

class WebInterpreter(callback: PageId => Unit)(implicit book: Book) extends Interpreter {
  override def apply[A](fa: InterpreterAction[A]): Id[A] = fa match {
    case Text(t)            => jQuery("#text").append(s"<p>$t</p>"): Unit
    case Choices(os) =>
      jQuery("#options").empty()
      if(os.nonEmpty)
        jQuery("#options").append(s"<ul>${
          os.map { o =>
            val node = jQuery(s"""<a href="${o.pid}">${o.desc}</a>""")
            node.click(_ => callback(o.pid))
          }

          os.map(o => s"""<li><a href="${o.pid}">${o.desc}</a></li>""").mkString
        }</ul>"): Unit
      else
        ()
  }
}
