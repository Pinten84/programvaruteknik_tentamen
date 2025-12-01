package se.lnu.boomerang.game;

import se.lnu.boomerang.model.Card;
import se.lnu.boomerang.model.Player;
import java.util.List;

/**
 * Interface for controlling a player (Human or Bot).
 * Handles input/output and decision making.
 */
public interface PlayerController {
    void setPlayer(Player player);

    Player getPlayer();

    void notifyMessage(String message);

    String requestInput(String prompt);

    /**
     * Selects a card to keep (draft) from the current hand.
     * 
     * @param hand The current hand.
     * @return The selected Card.
     */
    Card selectCardToDraft(List<Card> hand);

    /**
     * Selects the Throw card at the start of the round.
     * 
     * @param hand The initial hand.
     * @return The selected Throw card.
     */
    Card selectThrowCard(List<Card> hand);

    /**
     * Selects an activity to score.
     * 
     * @param options Map of Activity Name -> Score.
     * @return The name of the selected activity, or null/empty to skip.
     */
    String selectActivityToScore(java.util.Map<String, Integer> options);
}
