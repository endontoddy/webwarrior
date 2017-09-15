package webwarrior.books

import webwarrior._

object TheCave extends Book(
  Map(
    "1" -> Page(
      "You're standing in a big, dark cave",
      FinalPageAction.Choices(
        Choice("Go west", "2") ::
        Choice("Go east", "3") ::
        Nil
      )
    ),
    "2" -> Page(
      "You fall over and hurt your knee",
      FinalPageAction.Combat(
        Enemy("Bob", 8, 7) ::
        Enemy("Barry", 7, 10) ::
        Nil, Choice("Killed em all!", "4")
      )
    ),
    "3" -> Page(
      "Something is odd here...",
      FinalPageAction.TestLuck(
        success = Choice("You wander past a hole in the ground", "4"),
        failure = Choice("You fall in a spiked pit", "3.1")
      )
    ),
    "3.1" -> Page(
      "",
      FinalPageAction.Choices(Choice("You climb out and go on your way", "4") :: Nil),
      PageAction.Damage(3) :: Nil
    ),
    "4" -> Page(
      "You reach a dead end",
      FinalPageAction.End
    )
  ),
  "1"
)

