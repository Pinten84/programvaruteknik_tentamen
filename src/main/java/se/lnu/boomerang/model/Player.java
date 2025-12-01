package se.lnu.boomerang.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a player in the game.
 * Holds the state of the player's hand, draft, and score.
 */
public class Player {
    private final int id;
    private final boolean isBot;
    private List<Card> hand = new ArrayList<>();
    private List<Card> draft = new ArrayList<>();
    private List<Card> nextHand = new ArrayList<>();

    // Scoring state
    private Map<String, String> visitedSites = new HashMap<>(); // Letter -> Region
    private List<String> completedRegions = new ArrayList<>();
    private Map<String, Integer> activityScores = new HashMap<>();
    private int totalScore = 0;
    private int lastRoundScore = 0;

    // Round specific state
    private int regionRoundScore = 0;

    public Player(int id, boolean isBot) {
        this.id = id;
        this.isBot = isBot;
    }

    public int getId() {
        return id;
    }

    public boolean isBot() {
        return isBot;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = new ArrayList<>(hand);
    }

    public List<Card> getDraft() {
        return draft;
    }

    public void addToDraft(Card card) {
        this.draft.add(card);
    }

    public void clearDraft() {
        this.draft.clear();
    }

    public List<Card> getNextHand() {
        return nextHand;
    }

    public void setNextHand(List<Card> nextHand) {
        this.nextHand = new ArrayList<>(nextHand);
    }

    public void clearNextHand() {
        this.nextHand.clear();
    }

    public Map<String, String> getVisitedSites() {
        return visitedSites;
    }

    public void addVisitedSite(String letter, String region) {
        visitedSites.put(letter, region);
    }

    public List<String> getCompletedRegions() {
        return completedRegions;
    }

    public void addCompletedRegion(String region) {
        completedRegions.add(region);
    }

    public Map<String, Integer> getActivityScores() {
        return activityScores;
    }

    public void addActivityScore(String activity, int score) {
        activityScores.put(activity, score);
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void addScore(int score) {
        this.totalScore += score;
    }

    public int getLastRoundScore() {
        return lastRoundScore;
    }

    public void setLastRoundScore(int score) {
        this.lastRoundScore = score;
    }

    public int getRegionRoundScore() {
        return regionRoundScore;
    }

    public void addRegionRoundScore(int score) {
        this.regionRoundScore += score;
    }

    public void resetRegionRoundScore() {
        this.regionRoundScore = 0;
    }

    public Card removeCardFromHand(String letter) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getLetter().equalsIgnoreCase(letter)) {
                return hand.remove(i);
            }
        }
        return null;
    }

    public Card removeCardFromHand(int index) {
        if (index >= 0 && index < hand.size()) {
            return hand.remove(index);
        }
        return null;
    }
}
