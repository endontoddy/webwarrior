package webwarrior

import webwarrior.interpreters.WebInterpreter

class Executor(
                programBuilder: ProgramBuilder,
                interpreter: WebInterpreter
              ) {
  def apply(pid: PageId) = {
    val program = programBuilder(Page(
      "You enter a big dark cave",
      Nil,
      FinalPageAction.Choices(
        Choice("Light a match", "2") ::
        Choice("Stumble around in the dark", "3") ::
        Nil
      )
    ))
    program(()).foldMap(interpreter)
  }
}
