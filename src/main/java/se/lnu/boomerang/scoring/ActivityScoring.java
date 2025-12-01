package se.lnu.boomerang.scoring;

import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.model.Card;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ActivityScoring implements ScoringStrategy {
    private String description = "";
    private final int[] scoreTable = { 0, 2, 4, 7, 10, 15 };

    @Override
    public int calculateScore(Player player, List<Card> roundDraft) {
        // This method is tricky because it requires user input.
        // In this design, we assume the Game loop has already asked the user
        // and updated the player's state or we return 0 here and handle it separately.
        // For the sake of the interface, we'll return 0 and let the Game loop handle
        // the interactive part
        // using the helper methods below.
        return 0;
    }

    public Map<String, Integer> getAvailableOptions(Player player, List<Card> roundDraft) {
        Map<String, Integer> options = new HashMap<>();
        String[] activities = { "Indigenous Culture", "Bushwalking", "Swimming", "Sightseeing" };

        for (String act : activities) {
            if (!player.getActivityScores().containsKey(act)) {
                int count = 0;
                for (Card c : roundDraft) {
                    if (act.equals(c.getActivity())) {
                        count++;
                    }
                }
                if (count > 0) {
                    int scoreIndex = Math.min(count, 6) - 1;
                    if (scoreIndex >= 0) {
                        options.put(act, scoreTable[scoreIndex]);
                    }
                }
            }
        }
        return options;
    }

    @Override
    public String getCategoryName() {
        return "Activities score";
    }

    @Override
    public String getScoreDescription() {
        return description;
    }
}
