package cs3500.hw03;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import cs3500.hw02.Card;
import cs3500.hw02.PileType;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import cs3500.hw02.FreecellModel;

/**
 * A Test class for the free cell controller.
 */
public class FreecellControllerTest {
  FreecellModel model;
  FreecellController controller;
  FreecellModel expectedModel;

  /**
   * Creates the model, controller, and expectedModel.
   * @param input StringReader for the controller
   */
  public void initData(StringReader input) {
    model = new FreecellModel();
    expectedModel = new FreecellModel();
    controller = new FreecellController(input, new StringBuilder());
  }

  @Test
  public void testAllWorks() {
    StringReader input = new StringReader("C5 10 O1 q");
    this.initData(input);
    expectedModel.startGame(model.getDeck(), 5, 4, false);
    String state = expectedModel.getGameState();
    expectedModel.move(PileType.CASCADE, 4, 9, PileType.OPEN, 0);
    StringBuilder expectedOutput = new StringBuilder(state + "\n" + expectedModel.getGameState()
            + "\nGame quit prematurely.");
    controller.playGame(model.getDeck(), model, 5, 4, false);
    assertEquals(expectedOutput.toString(), controller.out.toString());
  }

  @Test
  public void playGameInvalidStartGameTest() {
    StringReader input = new StringReader("C5 10 O1 q");
    this.initData(input);
    ArrayList<Card> deck =
            new ArrayList<Card>(Arrays.asList(new Card("heart", 1), new Card("heart",
                    2)));
    StringBuilder expectedOutput = new StringBuilder("Could not start game.");
    controller.playGame(deck, model, 5, 4, false);
    assertEquals(expectedOutput.toString(), controller.out.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void playGameInvalidModelTest() {
    StringReader input = new StringReader("C5 10 O1 q");
    this.initData(input);
    controller.playGame(model.getDeck(), null, 5, 4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void playGameInvalidDeckTest() {
    StringReader input = new StringReader("C5 10 O1 q");
    this.initData(input);
    controller.playGame(null, model, 5, 4, false);
  }

  @Test
  public void playGameGameOverYesTest() {
    String move = "";
    for (int i = 0; i < 52; i++) {
      move += (" C" + (i + 1) + " 1 " + "F" + ((i % 4) + 1));
    }
    StringReader input = new StringReader(move);
    this.initData(input);
    this.controller.playGame(model.getDeck(), model, 52, 4, false);
    StringBuilder expectedOutput = new StringBuilder("");
    assertEquals(true, this.controller.out.toString().contains("Game over."));
  }
}
