package webwarrior

import cats.free.Free
import webwarrior.interpreters.{Interpreter, WebInterpreter}

class Executor(
                programBuilder: ProgramBuilder
              ) {

  def apply(pid: PageId)(implicit book: Book): Unit = {
    book.pages.get(pid) match {
      case Some(page) => apply(page)
      case None       => programBuilder.error(s"Page $pid not found")
    }
  }

  def apply(page: Page)(implicit book: Book): Unit = {
    val program: (Unit) => Free[Interpreter.InterpreterAction, Unit] = programBuilder(page)
    program(()).foldMap(new WebInterpreter(apply))
  }

  def apply(book: Book): Unit = {
    apply(book.initialPage)(book)
  }
}
