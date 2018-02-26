package cs3500.hw04;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import cs3500.hw02.Card;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.PileType;

public class FreecellMultiCardTest {
  @Test
  public void testSingleGameCreator() {
    assertTrue(FreecellModelCreator.create(FreecellModelCreator.GameType.SINGLEMOVE)
            instanceof FreecellModel);
  }

  @Test
  public void testMultiGameCreator() {
    assertTrue(FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE)
            instanceof FreecellMultiCard);
  }

  @Test
  public void createDeckTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
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
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 5, 4, false);
    assertNotEquals("", model.getGameState());
  }

  @Test
  public void validMoveCascadeToOpen5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 5, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToFilledCascade5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 5, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 2, 9, PileType.CASCADE, 4);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToEmptyCascade5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 2, 0, PileType.OPEN, 3);
    model.move(PileType.CASCADE, 3, 0, PileType.CASCADE, 2);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToEmptyFoundation5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test
  public void validMoveCascadeToFilledFoundation5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 52, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
    model.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveCascadeToFilledOpen5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 5, 4, false);
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 4, 8, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveCascadeToFilledFoundation5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 5, 4, false);
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 4, 8, PileType.FOUNDATION, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void invalidMoveCascadeToFilledCascade5Cascade4OpenTest() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    model.startGame(model.getDeck(), 5, 4, false);
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 4, 8, PileType.FOUNDATION, 0);
  }

  @Test
  public void testMultiMove2Cards1EmptyOpen0EmptyCascade() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    List<Card> deck = model.getDeck();
    Collections.reverse(deck);
    model.startGame(deck, 5, 4, false);
    String before = model.getGameState();
    model.move(PileType.CASCADE, 2, 9, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 3, 9, PileType.OPEN, 1);
    model.move(PileType.CASCADE, 4, 9, PileType.OPEN, 2);
    model.move(PileType.CASCADE, 0, 9, PileType.CASCADE, 2);
    String after = model.getGameState();
    assertNotEquals(after, before);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMove4CardsInvalidBuild2EmptyOpen() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    List<Card> deck = model.getDeck();
    Collections.reverse(deck);
    model.startGame(deck, 5, 4, false);
    model.move(PileType.CASCADE, 4, 9, PileType.CASCADE, 2);
    model.move(PileType.CASCADE, 4, 8, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 4, 7, PileType.OPEN, 1);
    model.move(PileType.CASCADE, 2, 7, PileType.CASCADE, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMove4CardsValidBuildInvalidMove2EmptyOpen() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    List<Card> deck = model.getDeck();
    Collections.reverse(deck);
    model.startGame(deck, 13, 4, false);
    model.move(PileType.CASCADE, 9, 3, PileType.CASCADE, 8);
    model.move(PileType.CASCADE, 8, 3, PileType.CASCADE, 1);
    model.move(PileType.CASCADE, 1, 3, PileType.CASCADE, 0);
    model.move(PileType.CASCADE, 2, 3, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 3, 3, PileType.OPEN, 1);
    model.move(PileType.CASCADE, 0, 3, PileType.OPEN, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMove4CardsInvalidBuildShuffled() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    List<Card> deck = model.getDeck();
    Collections.reverse(deck);
    model.startGame(deck, 5, 4, true);
    System.out.println(model.getGameState());
    model.move(PileType.CASCADE, 4, 6, PileType.CASCADE, 0);
  }


  @Test(expected = IllegalArgumentException.class)
  public void testMultiMove2CardsInvalidIntermediatePiles() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    List<Card> deck = model.getDeck();
    Collections.reverse(deck);
    model.startGame(deck, 5, 4, false);
    model.move(PileType.CASCADE, 1, 10, PileType.OPEN, 0);
    model.move(PileType.CASCADE, 1, 9, PileType.OPEN, 1);
    model.move(PileType.CASCADE, 1, 8, PileType.OPEN, 2);
    model.move(PileType.CASCADE, 1, 7, PileType.OPEN, 3);
    model.move(PileType.CASCADE, 0, 7, PileType.CASCADE, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMultiMove2CardsInvalidDestination() {
    FreecellModel model = FreecellModelCreator.create(FreecellModelCreator.GameType.MULTIMOVE);
    List<Card> deck = model.getDeck();
    Collections.reverse(deck);
    model.startGame(deck, 5, 4, false);
    model.move(PileType.CASCADE, 0, 9, PileType.OPEN, 0);
  }
}
