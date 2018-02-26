package cs3500.hw03;

import java.util.List;

import cs3500.hw02.FreecellOperations;

/**
 * Created by Aasish Basani on 5/22/2017.
 */
public interface IFreecellController<K> {
  /**
   * This method will start the Freecell game using the provided model.
   *
   * @param deck        the 52 card deck to be dealt
   * @param model       the free cell game model
   * @param numCascades the number of cascade piles to be built
   * @param numOpens    the number of open piles to be built
   * @param shuffle     if true, shuffle the deck else deal the deck as-is
   */
  void playGame(List<K> deck, FreecellOperations<K> model, int numCascades, int numOpens,
                boolean shuffle);


}
