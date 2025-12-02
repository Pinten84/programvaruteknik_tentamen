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
    }

    @Test
    public void testGameRequiresTwoToFourPlayers() {
        List<ScoringStrategy> strategies = new ArrayList<>();
        strategies.add(new ThrowCatchScoring());

        // Test with 1 player (should fail)
        List<PlayerController> onePlayer = new ArrayList<>();
        onePlayer.add(new MockController(0));
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Game(onePlayer, strategies);
        });

        // Test with 5 players (should fail)
        List<PlayerController> fivePlayers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            fivePlayers.add(new MockController(i));
        }
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Game(fivePlayers, strategies);
        });

        // Test with null (should fail)
        assertThrows(IllegalArgumentException.class, () -> {
            new Game(null, strategies);
        });
    }

    @Test
    public void testGameRequiresScoringStrategies() {
        List<PlayerController> controllers = new ArrayList<>();
        controllers.add(new MockController(0));
        controllers.add(new MockController(1));

        // Test with null strategies
        assertThrows(IllegalArgumentException.class, () -> {
            new Game(controllers, null);
        });

        // Test with empty strategies
        assertThrows(IllegalArgumentException.class, () -> {
            new Game(controllers, new ArrayList<>());
        });
    }

    @Test
    public void testPlayerHandManagement() {
        Player p = new Player(1, false);
        
        // Test adding cards to hand
        List<Card> hand = new ArrayList<>();
        hand.add(new Card("Site1", "A", "WA", 1, "", "", ""));
        hand.add(new Card("Site2", "B", "WA", 2, "", "", ""));
        p.setHand(hand);
        
        assertEquals(2, p.getHand().size());
        
        // Test removing card by letter
        Card removed = p.removeCardFromHand("A");
        assertNotNull(removed);
        assertEquals("A", removed.getLetter());
        assertEquals(1, p.getHand().size());
        
        // Test removing non-existent card
        Card notFound = p.removeCardFromHand("Z");
        assertNull(notFound);
        assertEquals(1, p.getHand().size());
    }

    @Test
    public void testDraftManagement() {
        Player p = new Player(1, false);
        
        Card card1 = new Card("Site1", "A", "WA", 1, "", "", "");
        Card card2 = new Card("Site2", "B", "WA", 2, "", "", "");
        
        p.addToDraft(card1);
        p.addToDraft(card2);
        
        assertEquals(2, p.getDraft().size());
        
        p.clearDraft();
        assertEquals(0, p.getDraft().size());
    }
}
