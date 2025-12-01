package se.lnu.boomerang.model;

import java.util.Objects;

/**
 * Represents a single card in the Boomerang game.
 * Immutable data class.
 */
public class Card {
    private final String name;
    private final String letter;
    private final String region;
    private final int number;
    private final String collection;
    private final String animal;
    private final String activity;

    public Card(String name, String letter, String region, int number, String collection, String animal, String activity) {
        this.name = name;
        this.letter = letter;
        this.region = region;
        this.number = number;
        this.collection = collection;
        this.animal = animal;
        this.activity = activity;
    }

    public String getName() { return name; }
    public String getLetter() { return letter; }
    public String getRegion() { return region; }
    public int getNumber() { return number; }
    public String getCollection() { return collection; }
    public String getAnimal() { return animal; }
    public String getActivity() { return activity; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(letter, card.letter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter);
    }

    @Override
    public String toString() {
        return String.format("%s [%s] (%d)", name, letter, number);
    }
}
