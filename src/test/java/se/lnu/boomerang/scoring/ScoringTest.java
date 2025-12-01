package se.lnu.boomerang.scoring;

import org.junit.jupiter.api.Test;
import se.lnu.boomerang.model.Card;
import se.lnu.boomerang.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringTest {

    @Test
    public void testAnimalScoring_Pairs() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("K1", "A", "WA", 1, "", "Kangaroos", ""));
        draft.add(new Card("K2", "B", "WA", 1, "", "Kangaroos", ""));

        AnimalScoring scorer = new AnimalScoring();
        assertEquals(3, scorer.calculateScore(p, draft));
    }

    @Test
    public void testAnimalScoring_MultiplePairs() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("E1", "A", "WA", 1, "", "Emus", ""));
        draft.add(new Card("E2", "B", "WA", 1, "", "Emus", ""));
        draft.add(new Card("E3", "C", "WA", 1, "", "Emus", ""));
        draft.add(new Card("E4", "D", "WA", 1, "", "Emus", ""));

        AnimalScoring scorer = new AnimalScoring();
        assertEquals(8, scorer.calculateScore(p, draft));
    }

    @Test
    public void testAnimalScoring_NoPairs() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("K1", "A", "WA", 1, "", "Kangaroos", ""));
        draft.add(new Card("E1", "B", "WA", 1, "", "Emus", ""));

        AnimalScoring scorer = new AnimalScoring();
        assertEquals(0, scorer.calculateScore(p, draft));
    }

    @Test
    public void testCollectionScoring_Doubling() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("L1", "A", "WA", 1, "Leaves", "", ""));
        draft.add(new Card("W1", "B", "WA", 1, "Wildflowers", "", ""));

        CollectionScoring scorer = new CollectionScoring();
        assertEquals(6, scorer.calculateScore(p, draft));
    }

    @Test
    public void testCollectionScoring_NoDoubling() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("S1", "A", "WA", 1, "Souvenirs", "", ""));
        draft.add(new Card("S2", "B", "WA", 1, "Souvenirs", "", ""));

        CollectionScoring scorer = new CollectionScoring();
        assertEquals(10, scorer.calculateScore(p, draft));
    }

    @Test
    public void testThrowCatchScoring() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("Throw", "A", "WA", 1, "", "", ""));
        for (int i = 0; i < 5; i++) {
            draft.add(new Card("Filler" + i, "F" + i, "WA", 2, "", "", ""));
        }
        draft.add(new Card("Catch", "C", "WA", 6, "", "", ""));

        ThrowCatchScoring scorer = new ThrowCatchScoring();
        assertEquals(5, scorer.calculateScore(p, draft));
    }

    @Test
    public void testTouristSiteScoring() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("Site1", "A", "WA", 1, "", "", ""));
        draft.add(new Card("Site2", "B", "WA", 1, "", "", ""));

        TouristSiteScoring scorer = new TouristSiteScoring();
        assertEquals(2, scorer.calculateScore(p, draft));

        assertTrue(p.getVisitedSites().containsKey("A"));
        assertTrue(p.getVisitedSites().containsKey("B"));

        List<Card> draft2 = new ArrayList<>();
        draft2.add(new Card("Site1", "A", "WA", 1, "", "", ""));
        draft2.add(new Card("Site3", "C", "WA", 1, "", "", ""));

        assertEquals(1, scorer.calculateScore(p, draft2));
    }

    @Test
    public void testActivityScoring_Options() {
        Player p = new Player(1, false);
        List<Card> draft = new ArrayList<>();
        draft.add(new Card("B1", "A", "WA", 1, "", "", "Bushwalking"));
        draft.add(new Card("B2", "B", "WA", 1, "", "", "Bushwalking"));
        draft.add(new Card("S1", "C", "WA", 1, "", "", "Swimming"));

        ActivityScoring scorer = new ActivityScoring();
        Map<String, Integer> options = scorer.getAvailableOptions(p, draft);

        assertTrue(options.containsKey("Bushwalking"));
        assertEquals(2, options.get("Bushwalking"));

        assertTrue(options.containsKey("Swimming"));
        assertEquals(0, options.get("Swimming"));

        p.addActivityScore("Bushwalking", 10);
        options = scorer.getAvailableOptions(p, draft);
        assertFalse(options.containsKey("Bushwalking"));
    }
}
