package webwarrior.interpreters

import cats.{Id, ~>}
import cats.free.Free
import org.scalajs.jquery.{JQueryEventObject, jQuery}
import webwarrior.{Adventurer, Book, PageId}
import webwarrior.interpreters.Interpreter.InterpreterAction
import webwarrior.interpreters.Interpreter.InterpreterAction._

class WebInterpreter extends Interpreter {

  override def apply(program: Adventurer => Free[Interpreter.InterpreterAction, Unit])
                  (implicit book: Book, adventurer: Adventurer): Unit =
    program(adventurer).foldMap(new (InterpreterAction ~> Id) {
      override def apply[A](fa: InterpreterAction[A]): Id[A] = fa match {
        case Text(t)            => text(t): Unit
        case Choices(os) =>
          jQuery("#options").empty()
          if(os.nonEmpty) {
            jQuery("#options").append("<ul></ul>")
            os.foreach { o =>
              jQuery("<li></li>").append(
                jQuery(s"""<a href="#">${o.desc}</a>""")
                  .click((e: JQueryEventObject) => {
                    e.preventDefault()
                    text(o.desc)
                    o.callback(adventurer)
                  })
              ).appendTo("#options ul")
            }
          }
          else ()
        case UpdateCharacterSheet(a) => updateCharacterSheet(a)
        case DisplayEnemies(es) => {
          jQuery("#arena table tr").slice(1).remove()
          es.foreach(e => jQuery("#arena table").append(
            s"""<tr><td>${e.name}</td><td>${e.skill}</td><td>${e.stamina}</td></tr>""")
          )
          jQuery("#arena").show(): Unit
        }
      }

    })

  override def initialise(adventurer: Adventurer, book: Book): Unit = {
    jQuery("#arena").hide()
    updateCharacterSheet(adventurer)
  }

  private def text(t: String) =
    jQuery("#text").append(s"<p>$t</p>")

  private def updateCharacterSheet(a: Adventurer): Unit = {
    jQuery("#iskill").text(a.initialSkill.toString)
    jQuery("#cskill").text(a.skill.toString)
    jQuery("#istamina").text(a.initialStamina.toString)
    jQuery("#cstamina").text(a.stamina.toString)
    jQuery("#iluck").text(a.initialLuck.toString)
    jQuery("#cluck").text(a.luck.toString)
  }
}
