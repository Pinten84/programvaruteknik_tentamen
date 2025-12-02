package se.lnu.boomerang.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a deck of cards.
 * Handles loading and shuffling.
 */
public class Deck {
    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(0);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    /**
     * Loads cards from a CSV file.
     * 
     * @param csvPath Path to the CSV file.
     * @return A new Deck.
     */
    public static Deck loadFromCSV(String csvPath) {
        List<Card> deck = new ArrayList<>();
        try (InputStream is = Deck.class.getResourceAsStream(csvPath)) {
            // If resource is null, try file system (for local run without packaging)
            if (is == null) {
                // Fallback to file system
                try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(csvPath))) {
                    return parseCSV(br);
                }
            }
            try (java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(is, StandardCharsets.UTF_8))) {
                return parseCSV(br);
            }
        } catch (Exception e) {
            e.printStackTrace();
                // Fallback to hardcoded deck if file fails.
            System.err.println("Failed to load CSV, using fallback deck.");
            return createAustraliaDeck();
        }
    }

    private static Deck parseCSV(java.io.BufferedReader br) throws java.io.IOException {
        List<Card> deck = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            // Skip header if present (check if line starts with "Name")
            if (line.startsWith("Name") || line.trim().isEmpty())
                continue;

            String[] parts = line.split(",");
            // Format: Name,Letter,Region,Number,Collection,Animal,Activity
            if (parts.length >= 7) {
                String name = parts[0].trim();
                String letter = parts[1].trim();
                String region = parts[2].trim();
                int number = Integer.parseInt(parts[3].trim());
                String collection = parts[4].trim();
                String animal = parts[5].trim();
                String activity = parts[6].trim();

                deck.add(new Card(name, letter, region, number, collection, animal, activity));
            }
        }
        return new Deck(deck);
    }

    /**
     * Factory method to create a standard Australia deck.
     * Used as fallback or default.
     */
    public static Deck createAustraliaDeck() {
        // ... (Keep existing hardcoded logic as fallback)
        List<Card> deck = new ArrayList<>();
        // Western Australia
        deck.add(new Card("The Bungle Bungles", "A", "Western Australia", 1, "Leaves", "", "Indigenous Culture"));
        deck.add(new Card("The Pinnacles", "B", "Western Australia", 1, "", "Kangaroos", "Sightseeing"));
        deck.add(new Card("Margaret River", "C", "Western Australia", 1, "Shells", "Kangaroos", ""));
        deck.add(new Card("Kalbarri National Park", "D", "Western Australia", 1, "Wildflowers", "", "Bushwalking"));

        // Northern Territory
        deck.add(new Card("Uluru", "E", "Northern Territory", 4, "", "Emus", "Indigenous Culture"));
        deck.add(new Card("Kakadu National Park", "F", "Northern Territory", 4, "", "Wombats", "Sightseeing"));
        deck.add(new Card("Nitmiluk National Park", "G", "Northern Territory", 4, "Shells", "Platypuses", ""));
        deck.add(new Card("King's Canyon", "H", "Northern Territory", 4, "", "Koalas", "Swimming"));

        // Queensland
        deck.add(new Card("The Great Barrier Reef", "I", "Queensland", 6, "Wildflowers", "", "Sightseeing"));
        deck.add(new Card("The Whitsundays", "J", "Queensland", 6, "", "Kangaroos", "Indigenous Culture"));
        deck.add(new Card("Daintree Rainforest", "K", "Queensland", 6, "Souvenirs", "", "Bushwalking"));
        deck.add(new Card("Surfers Paradise", "L", "Queensland", 6, "Wildflowers", "", "Swimming"));

        // South Australia
        deck.add(new Card("Barossa Valley", "M", "South Australia", 3, "", "Koalas", "Bushwalking"));
        deck.add(new Card("Lake Eyre", "N", "South Australia", 3, "", "Emus", "Swimming"));
        deck.add(new Card("Kangaroo Island", "O", "South Australia", 3, "", "Kangaroos", "Bushwalking"));
        deck.add(new Card("Mount Gambier", "P", "South Australia", 3, "Wildflowers", "", "Sightseeing"));

        // New South Wales
        deck.add(new Card("Blue Mountains", "Q", "New South Whales", 5, "", "Wombats", "Indigenous Culture"));
        deck.add(new Card("Sydney Harbour", "R", "New South Whales", 5, "", "Emus", "Sightseeing"));
        deck.add(new Card("Bondi Beach", "S", "New South Whales", 5, "", "Wombats", "Swimming"));
        deck.add(new Card("Hunter Valley", "T", "New South Whales", 5, "", "Emus", "Bushwalking"));

        // Victoria
        deck.add(new Card("Melbourne", "U", "Victoria", 2, "", "Wombats", "Bushwalking"));
        deck.add(new Card("The MCG", "V", "Victoria", 2, "Leaves", "", "Indigenous Culture"));
        deck.add(new Card("Twelve Apostles", "W", "Victoria", 2, "Shells", "", "Swimming"));
        deck.add(new Card("Royal Exhibition Building", "X", "Victoria", 2, "Leaves", "Platypuses", ""));

        // Tasmania
        deck.add(new Card("Salamanca Markets", "Y", "Tasmania", 7, "Leaves", "Emus", ""));
        deck.add(new Card("Mount Wellington", "Z", "Tasmania", 7, "", "Koalas", "Sightseeing"));
        deck.add(new Card("Port Arthur", "*", "Tasmania", 7, "Leaves", "", "Indigenous Culture"));
        deck.add(new Card("Richmond", "-", "Tasmania", 7, "", "Kangaroos", "Swimming"));

        return new Deck(deck);
    }
}
