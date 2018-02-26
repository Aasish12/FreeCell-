package cs3500.hw02;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

/**
 * This class test the Card class.
*/
public class CardTest {


  @Test
  public void correctCardTest() {
    Card c = new Card("heart", 1);
    assertEquals(1, c.value);
    assertEquals("heart", c.suit);
  }

  @Test(expected = IllegalArgumentException.class)
  public void incorrectValueLessCreateCardTest() {
    Card c = new Card("heart", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void incorrectValueMoreCreateCardTest() {
    Card c = new Card("heart", 14);
  }

  @Test(expected = IllegalArgumentException.class)
  public void incorrectSuitCreateCardTest() {
    Card c = new Card("laskdjfkas", 1);
  }

  @Test
  public void getColorRedTest() {
    Card c = new Card("heart", 1);
    assertEquals("red", c.getColor());
  }

  @Test
  public void getColorBlackTest() {
    Card c = new Card("club", 1);
    assertEquals("black", c.getColor());
  }

  @Test
  public void testCardToStringTest() {
    Card c = new Card("heart", 1);
    assertEquals("A♥", c.toString());
  }
}
