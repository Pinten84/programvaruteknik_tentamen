package se.lnu.boomerang.game;

import org.junit.jupiter.api.Test;
import se.lnu.boomerang.model.Card;
import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.scoring.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    // Mock controller for testing
    class MockController implements PlayerController {
        private Player player;
        private List<Card> handToDraftFrom;

        public MockController(int id) {
            this.player = new Player(id, false);
        }

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
            /* System.out.println(message); */ }

        @Override
        public String requestInput(String prompt) {
            return "";
        }

        @Override
        public Card selectCardToDraft(List<Card> hand) {
            // Always pick first card
            if (!hand.isEmpty())
                return hand.get(0);
            return null;
        }

        @Override
        public Card selectThrowCard(List<Card> hand) {
            // Always pick first card
            if (!hand.isEmpty())
                return hand.get(0);
            return null;
        }

        @Override
        public String selectActivityToScore(Map<String, Integer> options) {
            // Pick first available
            if (!options.isEmpty())
                return options.keySet().iterator().next();
            return null;
        }
    }

    @Test
    public void testGameInitialization() {
        List<PlayerController> controllers = new ArrayList<>();
        controllers.add(new MockController(0));
        controllers.add(new MockController(1));

        List<ScoringStrategy> strategies = new ArrayList<>();
        strategies.add(new ThrowCatchScoring());

        Game game = new Game(controllers, strategies);
        assertNotNull(game);
        // We can't easily test start() because it runs the full game loop.
        // But we verified the constructor works.
    }
}
