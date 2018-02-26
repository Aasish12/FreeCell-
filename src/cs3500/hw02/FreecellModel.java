package cs3500.hw02;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class FreecellModel implements FreecellOperations<Card> {
  /**
   *   Had to change the fields from private to protected, this is
   *   to allow the FreecellMultiCard access to the fields.
   */
  protected ArrayList<ArrayList<Card>> allfoundations = new ArrayList<ArrayList<Card>>();
  protected ArrayList<ArrayList<Card>> allopen = new ArrayList<ArrayList<Card>>();
  protected ArrayList<ArrayList<Card>> allcascades = new ArrayList<ArrayList<Card>>();

  public FreecellModel() {
    // default constructor
  }

  @Override
  public List<Card> getDeck() {
    ArrayList<Card> deck = new ArrayList<Card>();
    for (int i = 1; i <= 13; i++) {
      for (int j = 1; j <= 4; j++) {
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
    return deck;
  }

  @Override
  public void startGame(List<Card> deck, int numCascadePiles, int numOpenPiles, boolean shuffle) {
    allcascades = new ArrayList<ArrayList<Card>>(numCascadePiles);
    allopen = new ArrayList<ArrayList<Card>>(numOpenPiles);
    allfoundations = new ArrayList<ArrayList<Card>>(4);
    if (this.isValidDeck(deck)) {
      if (numCascadePiles < 4 || numOpenPiles < 1) {
        throw new IllegalArgumentException(
                "numCascadePiles must be >= 4 and numOpenPiles must be >= 1");
      }
      if (shuffle) {
        Collections.shuffle(deck);
      }

      for (int i = 0; i < numCascadePiles; i++) {
        ArrayList<Card> cascadePile = new ArrayList<Card>();
        int card = i;
        while (card < deck.size()) {
          cascadePile.add(deck.get(card));
          card += numCascadePiles;
        }
        allcascades.add(cascadePile);
      }

      for (int j = 0; j < numOpenPiles; j++) {
        allopen.add(new ArrayList<Card>());
      }

      for (int h = 0; h < 4; h++) {
        allfoundations.add(new ArrayList<Card>());
      }
    } else {
      throw new IllegalArgumentException("the deck is invalid");
    }
  }

  /**
   * This is a helper method for startGame. It checks if the given deck is valid (based on the
   * specification stated in getDeck)
   *
   * @param deck the deck to be dealt
   * @return boolean whether the given deck is valid
   */
  private boolean isValidDeck(List<Card> deck) {
    if (deck.size() == 52) {
      HashSet<Card> unique = new HashSet<Card>();
      for (int i = 0; i < 52; i++) {
        if (!unique.add(deck.get(i))) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) {

    if (pileNumber < 0 || cardIndex < 0 || destPileNumber < 0) {
      throw new IllegalArgumentException("Pick a card in a pile");
    }

    Card from = this.getFromCard(source, pileNumber, cardIndex);

    if (destination.equals(PileType.CASCADE)) {
      if (destPileNumber >= allcascades.size()) {
        throw new IllegalArgumentException("Pile Number out of bounds");
      }
      ArrayList<Card> cascadePile = allcascades.get(destPileNumber);
      if (cascadePile.size() == 0) {
        this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
      } else {
        Card to = cascadePile.get(cascadePile.size() - 1);
        if ((to.value - from.value) == 1
                && ((to.getColor().equals("red") && from.getColor().equals("black"))
                || (to.getColor().equals("black") && from.getColor().equals("red")))) {
          this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
        } else {
          throw new IllegalArgumentException("The card cannot be moved here");
        }
      }
    } else if (destination.equals(PileType.FOUNDATION)) {
      if (destPileNumber >= allfoundations.size()) {
        throw new IllegalArgumentException("Pile Number out of bounds");
      }
      ArrayList<Card> foundationPile = allfoundations.get(destPileNumber);
      boolean empty = false;
      if (foundationPile.size() == 0) {
        empty = true;
      }
      if (empty && from.value == 1) {
        boolean moveable = true;
        for (int i = 0; i < 4; i++) {
          ArrayList<Card> fSuit = allfoundations.get(i);
          if (fSuit.size() != 0) {
            Card check = allfoundations.get(i).get(0);
            if (check.suit.equals(from.suit)) {
              moveable = false;
            }
          }
        }
        if (moveable) {
          this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
        } else {
          throw new IllegalArgumentException("This suit already has a foundation pile");
        }
      } else if (empty) {
        throw new IllegalArgumentException("The foundation must start with A");
      } else {
        Card to = foundationPile.get(foundationPile.size() - 1);
        if (from.suit.equals(to.suit) && ((from.value - to.value) == 1)) {
          this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
        } else {
          throw new IllegalArgumentException(
                  "The card needs to be the same suit and one value less");
        }
      }
    } else if (destination.equals(PileType.OPEN)) {
      if (destPileNumber >= allopen.size()) {
        throw new IllegalArgumentException("Please choose a card in a pile to move");
      }
      ArrayList<Card> aOpenPile = allopen.get(destPileNumber);
      if (aOpenPile.size() == 0) {
        this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
      } else {
        throw new IllegalArgumentException("There is already a card here");
      }
    } else {
      throw new IllegalArgumentException("Not a valid destination");
    }
  }

  /**
   * Helper method to move, this actually moves the card from the source pile to the destination
   * pile. It will do this by removing the card from the source pile and adding the card to the
   * destination pile.
   * This method has been changed to protected so that the FreecellMultiCard does not need to
   * have repetitive code.
   *
   * @param source         the type of the source pile
   * @param pileNumber     the pile number of the given type, starting at 0
   * @param cardIndex      the index of the card to be moved from the source pile, starting at 0
   * @param destination    the type of the destination pile
   * @param destPileNumber the pile number of the given type, starting at 0
   * @param c              the card being moved from the bottom of the source pile
   */
  protected void moveHelp(PileType source, int pileNumber, int cardIndex, PileType destination,
                        int destPileNumber, Card c) {
    switch (source) {
      case CASCADE:
        allcascades.get(pileNumber).remove(c);
        switch (destination) {
          case CASCADE:
            allcascades.get(destPileNumber).add(c);
            break;
          case FOUNDATION:
            allfoundations.get(destPileNumber).add(c);
            break;
          case OPEN:
            allopen.get(destPileNumber).add(c);
            break;
          default:
            break;
        }
        break;
      case OPEN:
        allopen.get(pileNumber).remove(c);
        switch (destination) {
          case CASCADE:
            allcascades.get(destPileNumber).add(c);
            break;
          case FOUNDATION:
            allfoundations.get(destPileNumber).add(c);
            break;
          case OPEN:
            allopen.get(destPileNumber).add(c);
            break;
          default:
            break;
        }
        break;
      default:
        break;
    }
  }

  /**
   * This is a helper method to move that determines what the indicated card being moved is.
   * @param source     the type of the source pile
   * @param pileNumber the pile number of the given type, starting at 0
   * @param cardIndex  the index of the card to be moved from the source pile, starting at 0
   * @return the card that is being moved
   */
  private Card getFromCard(PileType source, int pileNumber, int cardIndex) {
    Card c;
    if (source.equals(PileType.OPEN)) {
      if (pileNumber >= allopen.size()) {
        throw new IllegalArgumentException("Please choose a card in a pile to move");
      }
      ArrayList<Card> aOpenPile = allopen.get(pileNumber);
      if (cardIndex != 0) {
        throw new IllegalArgumentException("Can only move the last card in the pile");
      }
      if (aOpenPile.size() == 0) {
        throw new IllegalArgumentException("There is no card in this pile");
      }
      return aOpenPile.get(cardIndex);

    } else if (source.equals(PileType.CASCADE)) {
      if (pileNumber >= allcascades.size()) {
        throw new IllegalArgumentException("Please choose a card in a pile to move");
      }
      ArrayList<Card> cascadePile = allcascades.get(pileNumber);
      if (cardIndex != cascadePile.size() - 1) {
        throw new IllegalArgumentException("Can only move the last card in the pile");
      }
      c = cascadePile.get(cardIndex);
      return c;
    } else {
      throw new IllegalArgumentException("cannot move this card");
    }
  }

  @Override
  public boolean isGameOver() {
    int totalCards = 0;
    for (ArrayList<Card> pile : allfoundations) {
      totalCards += pile.size();
    }
    return totalCards == 52;

  }

  @Override
  public String getGameState() {
    String state = "";
    if (allfoundations.isEmpty() && allopen.isEmpty() && allcascades.isEmpty()) {
      return state;
    }
    for (int i = 0; i < allfoundations.size(); i++) {
      ArrayList<Card> fPile = allfoundations.get(i);
      state += "F" + (i + 1);
      if (fPile.size() == 0) {
        state += ":";
      } else {
        state += ": ";
      }
      for (Card c : fPile) {
        state += c.toString();
        if (fPile.indexOf(c) != fPile.size() - 1) {
          state += ", ";
        }
      }
      state += "\n";
    }

    for (int j = 0; j < allopen.size(); j++) {
      ArrayList<Card> oPile = allopen.get(j);
      state += "O" + (j + 1);
      if (oPile.size() == 0) {
        state += ":";
      } else {
        state += ": ";
      }
      for (Card o : oPile) {
        state += o.toString();
      }
      state += "\n";
    }

    for (int k = 0; k < allcascades.size(); k++) {
      ArrayList<Card> cPile = allcascades.get(k);
      state += "C" + (k + 1);
      if (cPile.size() == 0) {
        state += ":";
      } else {
        state += ": ";
      }
      for (Card c : cPile) {
        state += c.toString();
        if (cPile.indexOf(c) != cPile.size() - 1) {
          state += ", ";
        }
      }
      if (allcascades.indexOf(cPile) != allcascades.size() - 1) {
        state += "\n";
      }
    }
    return state;
  }
}
