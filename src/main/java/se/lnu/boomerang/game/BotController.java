package se.lnu.boomerang.game;

import se.lnu.boomerang.model.Card;
import se.lnu.boomerang.model.Player;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BotController implements PlayerController {
    private Player player;
    private final Random random = new Random();

    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void notifyMessage(String message) {
        // Bots don't read messages, but we could log them if needed.
        // System.out.println("[Bot " + player.getId() + "] received: " + message);
    }

    @Override
    public String requestInput(String prompt) {
        return ""; // Bots don't type
    }

    @Override
    public Card selectCardToDraft(List<Card> hand) {
        // Simple bot: pick random card
        if (hand.isEmpty())
            return null;
        return hand.get(random.nextInt(hand.size()));
    }

    @Override
    public Card selectThrowCard(List<Card> hand) {
        if (hand.isEmpty())
            return null;
        return hand.get(random.nextInt(hand.size()));
    }

    @Override
    public String selectActivityToScore(Map<String, Integer> options) {
        // Simple bot: always pick the first available option, or the highest score
        if (options.isEmpty())
            return null;

        // Pick highest score
        String bestAct = null;
        int maxScore = -1;

        for (Map.Entry<String, Integer> entry : options.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                bestAct = entry.getKey();
            }
        }
        return bestAct; // Or "Y" if the prompt expects Y/N? The interface expects the activity name.
    }
}
