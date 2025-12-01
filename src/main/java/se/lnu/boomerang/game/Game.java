package se.lnu.boomerang.game;

import se.lnu.boomerang.model.*;
import se.lnu.boomerang.scoring.*;
import java.util.*;
import java.util.concurrent.*;

public class Game {
    private final List<PlayerController> controllers;
    private final Deck deck;
    private final List<ScoringStrategy> strategies;
    private final ExecutorService threadPool;
    private final List<String> finishedRegionsGlobal = new ArrayList<>();

    public Game(List<PlayerController> controllers, List<ScoringStrategy> strategies) {
        this.controllers = controllers;
        this.deck = Deck.createAustraliaDeck();
        this.strategies = strategies;
        this.threadPool = Executors.newFixedThreadPool(controllers.size());
    }

    public void start() {
        try {
            deck.shuffle();

            for (PlayerController pc : controllers) {
                List<Card> hand = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    hand.add(deck.draw());
                }
                pc.getPlayer().setHand(hand);
            }

            for (int round = 0; round < 4; round++) {
                playRound(round);

                if (round < 3) {
                    List<Card> allCards = Deck.createAustraliaDeck().getCards();
                    Collections.shuffle(allCards);

                    for (PlayerController pc : controllers) {
                        pc.getPlayer().clearDraft();
                        pc.getPlayer().clearNextHand();
                        List<Card> hand = new ArrayList<>();
                        for (int i = 0; i < 7; i++) {
                            hand.add(allCards.remove(0));
                        }
                        pc.getPlayer().setHand(hand);
                    }
                }
            }

            announceWinner();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

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
        // Region Bonuses
        String[] allRegions = { "Western Australia", "Northern Territory", "Queensland", "South Australia",
                "New South Whales", "Victoria", "Tasmania" };

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
}
