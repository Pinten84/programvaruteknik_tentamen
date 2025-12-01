package se.lnu.boomerang.scoring;

import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.model.Card;
import java.util.List;

public class ThrowCatchScoring implements ScoringStrategy {
    private String description = "";

    @Override
    public int calculateScore(Player player, List<Card> roundDraft) {
        if (roundDraft.size() < 7)
            return 0; // Should have 7 cards (Throw + 6 Drafted/Caught)

            // draft.get(0) is Throw, draft.get(6) is Catch. Assumes order is preserved.
        Card throwCard = roundDraft.get(0);
        Card catchCard = roundDraft.get(roundDraft.size() - 1);

        int score = Math.abs(throwCard.getNumber() - catchCard.getNumber());
        description = "Throw: " + throwCard.getNumber() + ", Catch: " + catchCard.getNumber() + " -> |"
                + throwCard.getNumber() + " - " + catchCard.getNumber() + "| = " + score;
        return score;
    }

    @Override
    public String getCategoryName() {
        return "Throw and Catch score";
    }

    @Override
    public String getScoreDescription() {
        return description;
    }
}
