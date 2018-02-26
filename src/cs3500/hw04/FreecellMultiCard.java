package cs3500.hw04;

import cs3500.hw02.Card;
import cs3500.hw02.FreecellModel;
import cs3500.hw02.PileType;

import java.util.ArrayList;

public class FreecellMultiCard extends FreecellModel {
  public FreecellMultiCard() {
    //default constructor
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) {
    if (pileNumber < 0 || cardIndex < 0 || destPileNumber < 0) {
      throw new IllegalArgumentException("Pick a card in a pile");
    }
    Card from = null;
    switch (source) {
      case CASCADE:
        if (pileNumber >= allcascades.size()) {
          throw new IllegalArgumentException("Please choose a card in a pile to move");
        }
        ArrayList<Card> cascadePile = allcascades.get(pileNumber);
        if (cardIndex >= cascadePile.size()) {
          throw new IllegalArgumentException("invalid cardIndex");
        }
        if (cardIndex == cascadePile.size() - 1) {
          from = cascadePile.get(cardIndex);
          break;
        } else {
          this.multiMove(cascadePile, pileNumber, cardIndex, destination, destPileNumber);
          return;
        }
      case OPEN:
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
        from = aOpenPile.get(cardIndex);
        break;
      default:
        throw new IllegalArgumentException("cannot move this card");
    }
    switch (destination) {
      case OPEN:
        if (destPileNumber >= allopen.size()) {
          throw new IllegalArgumentException("Please choose a destination pile to move to");
        }
        ArrayList<Card> aOpenPile = allopen.get(destPileNumber);
        if (aOpenPile.size() == 1) {
          throw new IllegalArgumentException("cannot move here");
        }
        this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
        break;
      case FOUNDATION:
        if (destPileNumber >= allfoundations.size()) {
          throw new IllegalArgumentException("Please choose a destination pile to move to");
        }
        ArrayList<Card> foundationPile = allfoundations.get(destPileNumber);
        if (foundationPile.size() == 0) {
          if (from.value == 1) {
            this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
            break;
          } else {
            throw new IllegalArgumentException("Cannot move that card to this foundation pile");
          }
        } else {
          Card to = foundationPile.get(foundationPile.size() - 1);
          if ((to.value - from.value) != 1 && from.suit != to.suit) {
            throw new IllegalArgumentException("cannot move that card here, it must be one value "
                    + "greater than the foundation and of the same suit");
          } else {
            this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
            break;
          }
        }
      case CASCADE:
        if (destPileNumber >= allcascades.size()) {
          throw new IllegalArgumentException("Please choose a destination pile to move to");
        }
        ArrayList<Card> cascadePile = allcascades.get(destPileNumber);
        if (cascadePile.isEmpty()) {
          this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
          break;
        }
        Card to = cascadePile.get(cascadePile.size() - 1);
        if (to.getColor() != from.getColor() && to.value - from.value == 1) {
          this.moveHelp(source, pileNumber, cardIndex, destination, destPileNumber, from);
          break;
        }
        throw new IllegalArgumentException("Cannot move this card here");
      default:
        throw new IllegalArgumentException("invalid destination");
    }
  }

  private void multiMove(ArrayList<Card> cascadePile, int pileNumber, int cardIndex, PileType
          destination, int destPileNumber) {
    if (destination.equals(PileType.CASCADE)) {
      if (destPileNumber >= allcascades.size()) {
        throw new IllegalArgumentException("Please choose a valid cascade pile to move builds "
                + "to");
      }
      if (cardIndex >= cascadePile.size()) {
        throw new IllegalArgumentException("card index out of bounds");
      }
      if (this.validBuild(cascadePile, cardIndex)) {
        Card from = cascadePile.get(cardIndex);
        ArrayList<Card> destPile = allcascades.get(destPileNumber);
        if (destPile.isEmpty()) {
          while (cardIndex < cascadePile.size()) {
            Card c = cascadePile.get(cardIndex);
            allcascades.get(pileNumber).remove(c);
            allcascades.get(destPileNumber).add(c);
          }
        } else {
          Card to = destPile.get(destPile.size() - 1);
          if (to.value - from.value == 1 && to.getColor() != from.getColor()) {
            while (cardIndex < cascadePile.size()) {
              Card c = cascadePile.get(cardIndex);
              allcascades.get(pileNumber).remove(c);
              allcascades.get(destPileNumber).add(c);
            }
          } else {
            throw new IllegalArgumentException("That is not a possible move, top card must be one "
                    + "lower than the destination and of a different color");
          }
        }
      } else {
        throw new IllegalArgumentException("This is not a valid build");
      }
    } else {
      throw new IllegalArgumentException("Cannot move multiple cards here");
    }
  }

  private boolean validBuild(ArrayList<Card> cascadePile, int cardIndex) {
    int emptyCascades = 0;
    for (ArrayList<Card> c : allcascades) {
      if (c.isEmpty()) {
        emptyCascades++;
      }
    }
    int emptyOpen = 0;
    for (ArrayList<Card> o : allopen) {
      if (o.isEmpty()) {
        emptyOpen++;
      }
    }
    if ((cascadePile.size() - cardIndex) > ((emptyOpen + 1) * Math.pow(2, emptyCascades))) {
      throw new IllegalArgumentException("Not enough intermediate piles to move this build");
    } else {
      for (int i = cardIndex; i < cascadePile.size() - 1; i++) {
        Card c = cascadePile.get(i);
        Card c2 = cascadePile.get(i + 1);
        if (c.getColor() == c2.getColor() || c.value - c2.value != 1) {
          return false;
        }
      }
      return true;
    }
  }
}

