package webwarrior

import cats.free.Free
import webwarrior.FinalPageAction.Combat
import webwarrior.interpreters.Interpreter

class Executor(
                programBuilder: ProgramBuilder,
                interpreter: Interpreter
              ) {

  def apply(book: Book): Unit = {
    val adventurer = Adventurer(10, 20, 10)
    interpreter.initialise(adventurer, book)
    apply(book.initialPage)(book, adventurer)
  }

  def apply(pid: PageId)
           (implicit book: Book, adventurer: Adventurer): Unit =
    book.pages.get(pid) match {
      case Some(page) => apply(page)
      case None       => runProgram(programBuilder.error(s"Page $pid not found"))
    }

  def apply(page: Page)
           (implicit book: Book, adventurer: Adventurer): Unit =
    runProgram(programBuilder(page)(
      (p,b,a) => apply(p)(b,a),
      (c,b,a) => apply(c)(b,a)
    ))

  def apply(c: Combat)
           (implicit book: Book, adventurer: Adventurer): Unit =
    runProgram(programBuilder.combat(c, (c,b,a) => apply(c)(b,a)))


  private def runProgram(p: (Adventurer) => Free[Interpreter.InterpreterAction, Unit])
                        (implicit book: Book, adventurer: Adventurer): Unit =
    interpreter(p)
}
