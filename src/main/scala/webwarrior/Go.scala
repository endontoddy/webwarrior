package webwarrior

import webwarrior.books.TheCave
import webwarrior.interpreters.WebInterpreter

object Go extends App {
  new Executor(new ProgramBuilder(), new WebInterpreter())(TheCave)
}
