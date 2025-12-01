package se.lnu.boomerang.scoring;

import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.model.Card;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class AnimalScoring implements ScoringStrategy {
    private String description = "";

    @Override
    public int calculateScore(Player player, List<Card> roundDraft) {
        Map<String, Integer> counts = new HashMap<>();
        String[] animals = { "Kangaroos", "Emus", "Wombats", "Koalas", "Platypuses" };
        int[] points = { 3, 4, 5, 7, 9 };

        for (Card c : roundDraft) {
            String animal = c.getAnimal();
            if (animal != null && !animal.isEmpty()) {
                counts.put(animal, counts.getOrDefault(animal, 0) + 1);
            }
        }

        int totalScore = 0;
        StringBuilder desc = new StringBuilder();

        for (int i = 0; i < animals.length; i++) {
            String animal = animals[i];
            int count = counts.getOrDefault(animal, 0);
                // Count pairs for each animal and calculate score accordingly.

            int pairs = count / 2;
            if (pairs > 0) {
                int score = pairs * points[i];
                totalScore += score;
                desc.append(animal).append(" x").append(pairs).append(" pairs (").append(score).append(") ");
            }
        }

        description = desc.toString();
        if (description.isEmpty())
            description = "No pairs";

        return totalScore;
    }

    @Override
    public String getCategoryName() {
        return "Animals score";
    }

    @Override
    public String getScoreDescription() {
        return description;
    }
}
