package webwarrior.books

import webwarrior.{Book, Choice, FinalPageAction, Page}

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
      FinalPageAction.End
    )
  ),
  "1"
)

