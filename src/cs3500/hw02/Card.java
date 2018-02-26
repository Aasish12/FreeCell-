package cs3500.hw02;

public class Card {
  public final String suit;
  public final int value;

  /**
   * The constructor takes in the suit as a String and the value of the card as an integer.
   * 
   * @param suit the suit of the card
   * @param value the value in a integer form
   */
  public Card(String suit, int value) {
    if (value > 13 || value < 1) {
      throw new IllegalArgumentException("The value can only be between 1 and 13");
    }
    this.value = value;
    switch (suit) {
      case ("heart"):
        this.suit = suit;
        break;
      case ("club"):
        this.suit = suit;
        break;
      case ("spade"):
        this.suit = suit;
        break;
      case ("diamond"):
        this.suit = suit;
        break;
      default:
        throw new IllegalArgumentException("The suit can only be a heart, club, spade, or diamond");
    }


  }

  /**
   * Takes the integer value of the card and returns the string value as follows.
   * 
   * <pre>
   * a 1 is a A 
   * a 11 is a J 
   * a 12 is a Q
   * a 13 is a K
   * all other numbers are the string value of the number
   * </pre>
   * 
   * @return the value of the card as above
   */
  private String getValueName() {
    switch (value) {
      case (1):
        return "A";
      case (11):
        return "J";
      case (12):
        return "Q";
      case (13):
        return "K";
      default:
        return Integer.toString(value);
    }
  }

  /**
   * Determines what color the card is, based on its suit value.
   * 
   * <pre>
   * if the suit is a heart of diamond than its color is red 
   * if the suit is a club or spade its color is black
   * </pre>
   */
  public String getColor() {
    switch (suit) {
      case ("heart"):
        return "red";
      case ("diamond"):
        return "red";
      case ("club"):
        return "black";
      case ("spade"):
        return "black";
      default:
        return "";
    }

  }

  /**
   * return the string that is showed on the face of a card.
   */
  public String toString() {
    return this.getValueName() + this.getSymbol();
  }

  /**
   * Return the symbol of the suit as follows.
   * 
   * <pre>
   * ♥ for a heart
   * ♣ for a club
   * ♠ for a spade 
   * ♦ for a diamond
   * </pre>
   * 
   * @return the symbol as above
   */
  private String getSymbol() {
    switch (this.suit) {
      case ("heart"):
        return "♥";
      case ("club"):
        return "♣";
      case ("spade"):
        return "♠";
      case ("diamond"):
        return "♦";
      default:
        return "";
    }
  }

  @Override
  public boolean equals(Object that) {
    if (that instanceof Card) {
      Card that1 = (Card) that;
      return this.suit.equals(that1.suit) && this.value == that1.value;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.value * this.suit.hashCode();
  }

}
