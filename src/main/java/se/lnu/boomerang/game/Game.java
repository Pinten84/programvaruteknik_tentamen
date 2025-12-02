package se.lnu.boomerang.game;

import se.lnu.boomerang.model.*;
import se.lnu.boomerang.scoring.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Game {
    private final List<PlayerController> controllers;
    private final Deck deck;
    private final List<ScoringStrategy> strategies;
    private final ExecutorService threadPool;
    private final List<String> finishedRegionsGlobal = new ArrayList<>();

    /**
     * Creates a new game with the specified controllers and scoring strategies.
     * 
     * @param controllers List of player controllers (must be 2-4 players)
     * @param strategies List of scoring strategies to apply each round
     * @throws IllegalArgumentException if player count is not 2-4
     */
    public Game(List<PlayerController> controllers, List<ScoringStrategy> strategies) {
        if (controllers == null || controllers.size() < 2 || controllers.size() > 4) {
            throw new IllegalArgumentException(
                "Game requires 2-4 players, got: " + (controllers == null ? "null" : controllers.size())
            );
        }
        if (strategies == null || strategies.isEmpty()) {
            throw new IllegalArgumentException("Game requires at least one scoring strategy");
        }
        
        this.controllers = controllers;
        this.deck = Deck.createAustraliaDeck();
        this.strategies = strategies;
        this.threadPool = Executors.newFixedThreadPool(controllers.size());
    }

    /**
     * Starts and runs the complete game (4 rounds).
     * Handles deck shuffling, card dealing, round execution, and winner announcement.
     */
    public void start() {
        try {
            deck.shuffle();

            // Deal initial hands
            for (PlayerController pc : controllers) {
                List<Card> hand = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    if (deck.isEmpty()) {
                        throw new IllegalStateException("Deck ran out of cards during initial deal");
                    }
                    hand.add(deck.draw());
                }
                pc.getPlayer().setHand(hand);
            }

            // Play 4 rounds
            for (int round = 0; round < 4; round++) {
                playRound(round);

                // Prepare next round (except after last round)
                if (round < 3) {
                    List<Card> allCards = Deck.createAustraliaDeck().getCards();
                    Collections.shuffle(allCards);

                    for (PlayerController pc : controllers) {
                        pc.getPlayer().clearDraft();
                        pc.getPlayer().clearNextHand();
                        List<Card> hand = new ArrayList<>();
                        for (int i = 0; i < 7; i++) {
                            if (allCards.isEmpty()) {
                                throw new IllegalStateException("Not enough cards for round " + (round + 2));
                            }
                            hand.add(allCards.remove(0));
                        }
                        pc.getPlayer().setHand(hand);
                    }
                }
            }

            announceWinner();

        } catch (IllegalStateException e) {
            System.err.println("Game error: " + e.getMessage());
            for (PlayerController pc : controllers) {
                pc.notifyMessage("Game ended due to error: " + e.getMessage());
            }
        } catch (InterruptedException e) {
            System.err.println("Game interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("Unexpected error during game: " + e.getMessage());
            e.printStackTrace();
            for (PlayerController pc : controllers) {
                pc.notifyMessage("Game ended unexpectedly. Please check logs.");
            }
        } finally {
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }
        }
    }

    /**
     * Executes a single round of the game.
     * 
     * Round flow:
     * 1. Each player selects a Throw card (hidden)
     * 2. Six drafting iterations:
     *    - Each player selects one card from hand
     *    - Pass remaining cards to next player (or previous for catch card)
     * 3. Scoring phase:
     *    - Calculate region bonuses
     *    - Apply all scoring strategies
     *    - Handle activity scoring (player choice)
     * 
     * @param roundNr The round number (0-3)
     * @throws InterruptedException if thread execution is interrupted
     * @throws ExecutionException if thread execution fails
     */
    private void playRound(int roundNr) throws InterruptedException, ExecutionException {
        // 1. Select Throw Card
        List<Callable<Void>> throwTasks = new ArrayList<>();
        for (PlayerController pc : controllers) {
            throwTasks.add(() -> {
                pc.notifyMessage("Round " + (roundNr + 1) + " started.");
                pc.notifyMessage("Select your Throw card (will be hidden).");
                pc.notifyMessage(printCards(pc.getPlayer().getHand()));
                pc.notifyMessage("Type the letter of the card to draft");

                Card throwCard = pc.selectThrowCard(pc.getPlayer().getHand());
                if (throwCard != null) {
                    pc.getPlayer().removeCardFromHand(throwCard.getLetter());
                    pc.getPlayer().addToDraft(throwCard);
                }
                return null;
            });
        }
        threadPool.invokeAll(throwTasks);

        // 2. Drafting Phase
        for (int i = 0; i < 6; i++) {
            // Pass hands logic
            for (int p = 0; p < controllers.size(); p++) {
                PlayerController current = controllers.get(p);
                // Pass to NEXT player (index + 1)
                PlayerController next = controllers.get((p + 1) % controllers.size());

                if (i == 5) {
                    // Last card passed to PREVIOUS player
                    int prevIndex = (p - 1 + controllers.size()) % controllers.size();
                    PlayerController prev = controllers.get(prevIndex);
                    prev.getPlayer().setNextHand(current.getPlayer().getHand());
                } else {
                    next.getPlayer().setNextHand(current.getPlayer().getHand());
                }
            }

            for (PlayerController pc : controllers) {
                pc.getPlayer().setHand(pc.getPlayer().getNextHand());
                pc.getPlayer().clearNextHand();
            }

            if (i == 5) {
                // Catch card
                for (PlayerController pc : controllers) {
                    if (!pc.getPlayer().getHand().isEmpty()) {
                        Card catchCard = pc.getPlayer().getHand().get(0);
                        pc.getPlayer().addToDraft(catchCard);
                        pc.getPlayer().getHand().clear();
                        pc.notifyMessage("Catch card received: " + catchCard);
                    }
                }
            } else {
                // Draft
                List<Callable<Void>> draftTasks = new ArrayList<>();
                for (PlayerController pc : controllers) {
                    draftTasks.add(() -> {
                        pc.notifyMessage("\n*****************************\nYour current draft: \n"
                                + printCards(pc.getPlayer().getDraft()));
                        if (!pc.getPlayer().getVisitedSites().isEmpty()) {
                            pc.notifyMessage("Sites from previous rounds:\n"
                                    + new TreeMap<>(pc.getPlayer().getVisitedSites()) + "\n");
                        }

                        pc.notifyMessage("\nYour current hand: \n" + printCards(pc.getPlayer().getHand()));
                        pc.notifyMessage("Type the letter of the card to draft");

                        Card picked = pc.selectCardToDraft(pc.getPlayer().getHand());
                        if (picked != null) {
                            pc.getPlayer().removeCardFromHand(picked.getLetter());
                            pc.getPlayer().addToDraft(picked);
                        }
                        return null;
                    });
                }
                threadPool.invokeAll(draftTasks);

                // Show drafted cards
                for (PlayerController pc : controllers) {
                    for (PlayerController other : controllers) {
                        List<Card> visibleDraft = new ArrayList<>(other.getPlayer().getDraft());
                        if (!visibleDraft.isEmpty())
                            visibleDraft.remove(0); // Hide throw
                        pc.notifyMessage(
                                "\nPlayer " + other.getPlayer().getId() + " has drafted\n" + printCards(visibleDraft));
                    }
                }
            }
        }

        // 3. Scoring
        // Region Bonuses - Get regions from deck to support different game editions
        String[] allRegions = getRegionsFromDeck();

        for (String region : allRegions) {
            boolean regionComplete = false;
            for (PlayerController pc : controllers) {
                Player p = pc.getPlayer();
                // Check if player completed this region
                if (!finishedRegionsGlobal.contains(region) && checkRegionComplete(p, region)) {
                    p.addCompletedRegion(region);
                    p.addRegionRoundScore(3);
                    regionComplete = true;
                }
            }
            if (regionComplete) {
                finishedRegionsGlobal.add(region);
            }
        }

        for (PlayerController pc : controllers) {
            Player p = pc.getPlayer();
            pc.notifyMessage(
                    "********************************\nYour draft this round: \n" + printCards(p.getDraft()) + "\n");

            int roundScore = 0;

            for (ScoringStrategy strategy : strategies) {
                int score = strategy.calculateScore(p, p.getDraft());
                pc.notifyMessage(strategy.getScoreDescription());
                roundScore += score;
            }

            // Activity Scoring
            ActivityScoring actScoring = new ActivityScoring();
            Map<String, Integer> options = actScoring.getAvailableOptions(p, p.getDraft());
            pc.notifyMessage("This round you have gathered the following new activities:");
            // We need to construct the message string for options
            StringBuilder actMsg = new StringBuilder();
            for (Map.Entry<String, Integer> entry : options.entrySet()) {
                actMsg.append(entry.getKey()).append("(# ").append(getCountForScore(entry.getValue())).append(")\t");
            }
            pc.notifyMessage(actMsg.toString());
            pc.notifyMessage("\nSelect if you wish to score one of them");

            String selectedAct = pc.selectActivityToScore(options);
            if (selectedAct != null && options.containsKey(selectedAct)) {
                int score = options.get(selectedAct);
                p.addActivityScore(selectedAct, score);
                pc.notifyMessage("This round you scored this activity: " + selectedAct + "[" + score + " points]");
                roundScore += score;
            }

            p.setLastRoundScore(roundScore);
            p.addScore(roundScore);
            p.resetRegionRoundScore();

            pc.notifyMessage("The following regions have now been completed: " + finishedRegionsGlobal);
        }
    }

    /**
     * Checks if a player has completed a region (visited all 4 sites).
     * 
     * @param p The player to check
     * @param region The region name
     * @return true if all 4 sites in the region have been visited
     */
    private boolean checkRegionComplete(Player p, String region) {
        Set<String> sitesInRegion = new HashSet<>();
        // Check visited
        for (Map.Entry<String, String> entry : p.getVisitedSites().entrySet()) {
            if (entry.getValue().equals(region)) {
                sitesInRegion.add(entry.getKey());
            }
        }
        // Check draft
        for (Card c : p.getDraft()) {
            if (c.getRegion().equals(region)) {
                sitesInRegion.add(c.getLetter());
            }
        }

        return sitesInRegion.size() == 4;
    }

    /**
     * Determines and announces the winner.
     * Winner is the player with highest total score.
     * Tiebreaker: Throw & Catch score (not implemented in this version).
     */
    private void announceWinner() {
        Player winner = controllers.get(0).getPlayer();
        for (PlayerController pc : controllers) {
            if (pc.getPlayer().getTotalScore() > winner.getTotalScore()) {
                winner = pc.getPlayer();
            }
        }

        for (PlayerController pc : controllers) {
            pc.notifyMessage("The winner is player: " + winner.getId() + " with " + winner.getTotalScore() + " points");
        }
    }

    /**
     * Formats a list of cards for display.
     * Shows site name, letter, number, region, collections, animals, and activities.
     * 
     * @param cards The cards to format
     * @return Formatted string representation
     */
    private String printCards(List<Card> cards) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%27s", "Site [letter] (nr):  "));
        for (Card c : cards)
            sb.append(String.format("%-35s", c.getName() + " [" + c.getLetter() + "] (" + c.getNumber() + ")"));
        sb.append("\n");
        sb.append(String.format("%27s", "Region:  "));
        for (Card c : cards)
            sb.append(String.format("%-35s", c.getRegion()));
        sb.append("\n");
        sb.append(String.format("%27s", "Collections:  "));
        for (Card c : cards)
            sb.append(String.format("%-35s", c.getCollection()));
        sb.append("\n");
        sb.append(String.format("%27s", "Animals:  "));
        for (Card c : cards)
            sb.append(String.format("%-35s", c.getAnimal()));
        sb.append("\n");
        sb.append(String.format("%27s", "Activities:  "));
        for (Card c : cards)
            sb.append(String.format("%-35s", c.getActivity()));
        return sb.toString();
    }

    private int getCountForScore(int score) {
        int[] scoreTable = { 0, 2, 4, 7, 10, 15 };
        for (int i = 0; i < scoreTable.length; i++) {
            if (scoreTable[i] == score)
                return i + 1;
        }
        return 0;
    }

    /**
     * Extracts unique regions from the deck.
     * This makes the game extensible to different editions (Europe, USA) without hardcoding regions.
     * 
     * @return Array of unique region names from the deck
     */
    private String[] getRegionsFromDeck() {
        Set<String> regionSet = new HashSet<>();
        for (Card card : deck.getCards()) {
            if (card.getRegion() != null && !card.getRegion().isEmpty()) {
                regionSet.add(card.getRegion());
            }
        }
        return regionSet.toArray(new String[0]);
    }
}
