package webwarrior

import webwarrior.interpreters.WebInterpreter

object Go extends App {

  new Executor(new ProgramBuilder(), new WebInterpreter())("1")
}
