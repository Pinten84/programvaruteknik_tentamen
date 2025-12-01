package se.lnu.boomerang.scoring;

import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.model.Card;
import java.util.List;

/**
 * Interface for scoring strategies.
 * Allows different game editions to implement different scoring rules.
 */
public interface ScoringStrategy {
    /**
     * Calculates the score for a specific category.
     * 
     * @param player     The player to score.
     * @param roundDraft The cards drafted in the current round.
     * @return The calculated score.
     */
    int calculateScore(Player player, List<Card> roundDraft);

    /**
     * Gets the name of the scoring category.
     * 
     * @return The category name.
     */
    String getCategoryName();

    /**
     * Gets a description of the score for display purposes.
     * 
     * @return A string describing how the score was calculated.
     */
    String getScoreDescription();
}
