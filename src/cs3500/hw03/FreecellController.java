package cs3500.hw03;

import cs3500.hw02.FreecellOperations;
import cs3500.hw02.Card;
import cs3500.hw02.PileType;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * The controller for the free cell game.
 */
public class FreecellController implements IFreecellController<Card> {
  public final Readable in;
  public final Appendable out;

  /**
   * The constructor for the controller.
   *
   * @param rd the reader for input the user types
   * @param ap the output to present the user
   */
  public FreecellController(Readable rd, Appendable ap) {
    if (rd == null || ap == null) {
      throw new IllegalStateException();
    }
    this.in = rd;
    this.out = ap;
  }


  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, int numCascades,
                       int numOpens, boolean shuffle) {
    if (deck == null || model == null) {
      throw new IllegalArgumentException("The deck and model cannot be null");
    }
    try {
      model.startGame(deck, numCascades, numOpens, shuffle);
      this.add(model.getGameState());
    } catch (IllegalArgumentException e) {
      this.add("Could not start game.");
      return;
    }
    PileType source = null;
    PileType destination = null;
    int cardIndex = -205;
    int destPileNumber = -205;
    int pileNumber = -205;
    boolean premature = false;
    Scanner scan = new Scanner(this.in);
    System.out.println("Please input a source and the source pile number, with no space "
            + "between them");
    while (!model.isGameOver() && scan.hasNext() && !premature) {
      boolean correctSource = true;
      while (correctSource) {
        String from = scan.next();
        if (from.equalsIgnoreCase("q") || from.equalsIgnoreCase("quit")) {
          this.add("\nGame quit prematurely.");
          premature = true;
          return;
        }
        switch (from.charAt(0)) {
          case ('C'):
            source = PileType.CASCADE;
            break;
          case ('O'):
            source = PileType.OPEN;
            break;
          default:
            System.out.println("Please enter a valid source, the source can only  be a cascade"
                    + " or an open pile");
            break;
        }
        try {
          pileNumber = Integer.parseInt(from.substring(1)) - 1;
          correctSource = false;
        } catch (NumberFormatException e) {
          System.out.println("Please enter a valid source, the source can only  be a cascade"
                  + " or an open pile");
        }
      }

      System.out.println("Please input a cardIndex ");
      boolean correctIndex = true;
      while (scan.hasNext() && correctIndex) {
        String index = scan.next();
        try {
          cardIndex = Integer.parseInt(index) - 1;
          correctIndex = false;
        } catch (NumberFormatException e) {
          if (index.equalsIgnoreCase("q")
                  || index.equalsIgnoreCase("quit")) {
            this.add("\nGame quit prematurely.");
            premature = true;
            correctIndex = false;
          }
        }
      }

      boolean correctDestination = true;
      System.out.println("Please input a Destination");
      while (scan.hasNext() && correctDestination) {
        String to = scan.next();
        if (to.equalsIgnoreCase("q") || to.equalsIgnoreCase("quit")) {
          this.add("\nGame quit prematurely.");
          premature = true;
          return;
        }
        switch (to.charAt(0)) {
          case ('C'):
            destination = PileType.CASCADE;
            try {
              destPileNumber = Integer.parseInt(to.substring(1)) - 1;
              correctDestination = false;
            } catch (StringIndexOutOfBoundsException ce) {
              System.out.println("please input a valid destPileNumber");
            }
            break;
          case ('O'):
            destination = PileType.OPEN;
            try {
              destPileNumber = Integer.parseInt(to.substring(1)) - 1;
              correctDestination = false;
            } catch (NumberFormatException ce) {
              System.out.println("please input a valid destPileNumber");
            }
            break;
          case ('F'):
            destination = PileType.FOUNDATION;
            try {
              destPileNumber = Integer.parseInt(to.substring(1)) - 1;
              correctDestination = false;
            } catch (StringIndexOutOfBoundsException ce) {
              System.out.println("please input a valid destPileNumber");
            }
            break;
          default:
            System.out.println("Please enter a valid destination, the destination must be an open"
                    + ", cascade, or foundation pile ");
            break;
        }
      }
      if (premature) {
        return;
      }
      try {
        model.move(source, pileNumber, cardIndex, destination, destPileNumber);
        this.add("\n" + model.getGameState());
      } catch (IllegalArgumentException e) {
        this.add("Invalid move. Try again" + e);
      }
    }
    if (premature) {
      return;
    }
    if (model.isGameOver()) {
      this.add(model.getGameState() + "\nGame over.");
    }

  }

  /**
   *
   * @param s is the given String that is to be appendend to the appendable object.
   */
  private void add(String s) {
    try {
      this.out.append(s);
    } catch (IOException e) {
      // not good
    }
  }
}
