package webwarrior

import scala.util.Random

class DiceRoll(rng: Random = new Random()) {

  def apply(numberOfDice: Int = 1, sides: Int = 6): List[Int] =
    List.fill(numberOfDice)(rng.nextInt(sides) + 1)
}
