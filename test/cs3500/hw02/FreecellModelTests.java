package cs3500.hw02;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class FreecellModelTests {
  @Test
  public void createDeckTest() {
    FreecellModel model = new FreecellModel();
    List<Card> deck = model.getDeck();
    assertEquals(52, deck.size());
    HashSet<Card> unique = new HashSet<Card>();
    boolean validDeck = true;
    for (int i = 0; i < 52; i++) {
      if (!unique.add(deck.get(i))) {
        validDeck = false;
      }
    }
    assertEquals(true, validDeck);
  }

  @Test
  public void startGameCorrectlyTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 4, false);
    assertNotEquals("", model.getGameState());
  }

  @Test
  public void startGameNotCalledTest() {
    FreecellModel model = new FreecellModel();
    assertEquals("", model.getGameState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void startGameIncorrectDeckWrongSize() {
    FreecellModel model = new FreecellModel();
    ArrayList<Card> deck =
            new ArrayList<Card>(Arrays.asList(new Card("heart", 1), new Card("heart", 2)));
    model.startGame(deck, 5, 4, false);

    assertEquals("", model.getGameState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void startGameIncorrectDeckDuplicateCard() {
    FreecellModel model = new FreecellModel();
    ArrayList<Card> deck = new ArrayList<Card>();
    for (int i = 0; i < 52; i++) {
      deck.add(new Card("heart", 1));
    }
    model.startGame(deck, 5, 4, false);

    assertEquals("", model.getGameState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void startGameIncorrectnumCascadePiles() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 3, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void startGameIncorrectnumOpenPiles() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 0, false);
  }

  @Test
  public void startGameShuffleTest() {
    FreecellModel shuffledmodel = new FreecellModel();
    FreecellModel unshuffledmodel = new FreecellModel();
    shuffledmodel.startGame(shuffledmodel.getDeck(), 5, 4, true);
    unshuffledmodel.startGame(unshuffledmodel.getDeck(), 5, 4, false);
    assertEquals(false, shuffledmodel.getGameState().equals(unshuffledmodel.getGameState()));
  }

  @Test
  public void validMoveCascadeToOpen5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToFilledCascade5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 2, 9, PileType.CASCADE, 4);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToEmptyCascade5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 2, 0, PileType.OPEN, 3);
    model.move(PileType.CASCADE, 3, 0, PileType.CASCADE, 2);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToEmptyFoundation5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToFilledFoundation5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveCascadeToFilledOpen5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 4, false);
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 4, 8, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveCascadeToFilledFoundation5Cascade4OpenTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 4, false);
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 4, 8, PileType.FOUNDATION, 0);
  }

  @Test
  public void validMoveOpenToEmptyCascadeTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 1, 0, PileType.OPEN, 1);
    model.move(PileType.OPEN, 1, 0, PileType.CASCADE, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }


  @Test
  public void validMoveOpenToFilledCascadeTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0, PileType.CASCADE, 7);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenToFilledCascadeTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0, PileType.CASCADE, 1);
  }


  @Test
  public void validMoveOpenToEmptyFoundationTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.OPEN, 0);
    model.move(PileType.OPEN, 0, 0, PileType.FOUNDATION, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveOpenToFilledFoundationTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenToFilledFoundationTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 1, 0, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveOpenToEmptyFoundationTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveFoundationTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalideMovePileNumber() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, 53, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalideMoveNegativePileNumber() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, -1, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalideMoveCardIndex() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, 0, 1, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalideMoveNegativeCardIndex() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, 0, -1, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalideMoveDestPileNumber() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalideMoveNegativeDestPileNumber() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 52, 4, false);
    model.move(PileType.FOUNDATION, 0, 0, PileType.OPEN, -5);
  }

  @Test
  public void isGameOverYesTest() {
    ArrayList<Card> deck = new ArrayList<Card>();
    for (int j = 0; j < 4; j++) {
      for (int i = 13; i > 0; i--) {
        switch (j) {
          case (1):
            deck.add(new Card("heart", i));
            break;
          case (2):
            deck.add(new Card("diamond", i));
            break;
          case (3):
            deck.add(new Card("club", i));
            break;
          default:
            deck.add(new Card("spade", i));
            break;
        }
      }
    }
    FreecellModel model = new FreecellModel();
    model.startGame(deck, 4, 4, false);
    int count = 0;
    int destnum = -1;
    for (int k = 12; k >= 0; k--) {
      for (int h = 3; h >= 0; h--) {
        if (count % 13 == 0) {
          destnum++;
        }
        count++;
        model.move(PileType.CASCADE, h, k, PileType.FOUNDATION, destnum);
      }

    }
    assertEquals(true, model.isGameOver());
  }

  @Test
  public void isGameOverNoTest() {
    FreecellModel model = new FreecellModel();
    model.startGame(model.getDeck(), 5, 4, false);
    model.move(PileType.CASCADE, 2, 9, PileType.CASCADE, 4);
    assertEquals(false, model.isGameOver());
  }



}
