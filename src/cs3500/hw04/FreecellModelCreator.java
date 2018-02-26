package cs3500.hw04;

import cs3500.hw02.FreecellModel;

public class FreecellModelCreator {
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

  /**
   * Will create a free cell game that accepts multiple card movements at once or only one at a
   * time, depending on what the GameType is.
   *
   * @param type Is either a singlemove or a multimove
   * @return FreecellModel
   */
  public static FreecellModel create(GameType type) {
    if (type.equals(GameType.SINGLEMOVE)) {
      return new FreecellModel();
    } else if (type.equals(GameType.MULTIMOVE)) {
      return new FreecellMultiCard();
    } else {
      throw new IllegalArgumentException("Not a valid game type");
    }
  }
}