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
                    // Update visited sites for scoring.
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
