package se.lnu.boomerang.scoring;

import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.model.Card;
import java.util.List;

public class CollectionScoring implements ScoringStrategy {
    private String description = "";

    @Override
    public int calculateScore(Player player, List<Card> roundDraft) {
        int leaves = countItems(roundDraft, "Leaves");
        int wildflowers = countItems(roundDraft, "Wildflowers");
        int shells = countItems(roundDraft, "Shells");
        int souvenirs = countItems(roundDraft, "Souvenirs");

        int sum = (leaves * 1) + (wildflowers * 2) + (shells * 3) + (souvenirs * 5);
        int finalScore = (sum <= 7) ? sum * 2 : sum;

        description = String.format("L:%d W:%d S:%d Sov:%d -> Sum: %d -> Score: %d",
                leaves, wildflowers, shells, souvenirs, sum, finalScore);

        return finalScore;
    }

    private int countItems(List<Card> cards, String item) {
        int count = 0;
        for (Card c : cards) {
            if (item.equals(c.getCollection())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String getCategoryName() {
        return "Collections score";
    }

    @Override
    public String getScoreDescription() {
        return description;
    }
}
