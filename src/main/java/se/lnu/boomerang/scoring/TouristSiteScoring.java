package se.lnu.boomerang.scoring;

import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.model.Card;
import java.util.List;
import java.util.Map;

public class TouristSiteScoring implements ScoringStrategy {
    private String description = "";

    @Override
    public int calculateScore(Player player, List<Card> roundDraft) {
        int newSites = 0;
        Map<String, String> visited = player.getVisitedSites();

        for (Card card : roundDraft) {
            if (!visited.containsKey(card.getLetter())) {
                newSites++;
                // Note: We don't update the player state here, just calculate.
                // The game loop should handle state updates to avoid side effects in
                // calculation?
                // Actually, for this simple game, updating here might be easier, but let's
                // stick to calculation.
                // Wait, if we don't update, we can't track it.
                // Let's assume the Game loop calls a separate method to update state, OR we
                // allow side effects here.
                // Given the original code updates state during scoring, we will do the same but
                // be careful.
                player.addVisitedSite(card.getLetter(), card.getRegion());
            }
        }

        int regionBonus = player.getRegionRoundScore(); // This is calculated separately in the game loop before scoring

        int total = newSites + regionBonus;
        description = newSites + " new sites + " + regionBonus + " region bonus";
        return total;
    }

    @Override
    public String getCategoryName() {
        return "Tourist sites score";
    }

    @Override
    public String getScoreDescription() {
        return description;
    }
}
