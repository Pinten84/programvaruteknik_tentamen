package se.lnu.boomerang.game;

import se.lnu.boomerang.model.Card;
import se.lnu.boomerang.model.Player;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleController implements PlayerController {
    private Player player;
    private final Scanner in;
    private final PrintWriter out;

    public ConsoleController(Scanner in, PrintWriter out) {
        this.in = in;
        this.out = out;
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
        out.println(message);
        out.flush();
    }

    @Override
    public String requestInput(String prompt) {
        out.println(prompt);
        out.flush();
        if (in.hasNextLine()) {
            return in.nextLine();
        }
        return "";
    }

    @Override
    public Card selectCardToDraft(List<Card> hand) {
        String response = "";
        while (true) {
            if (in.hasNextLine()) {
                response = in.nextLine();
            }

            for (Card c : hand) {
                if (c.getLetter().equalsIgnoreCase(response)) {
                    return c;
                }
            }
            out.println("Invalid card letter. Try again:");
            out.flush();
        }
    }

    @Override
    public Card selectThrowCard(List<Card> hand) {
        String response = "";
        while (true) {
            if (in.hasNextLine()) {
                response = in.nextLine();
            }
            for (Card c : hand) {
                if (c.getLetter().equalsIgnoreCase(response)) {
                    return c;
                }
            }
            out.println("Invalid card letter. Try again:");
            out.flush();
        }
    }

    @Override
    public String selectActivityToScore(Map<String, Integer> options) {
        for (Map.Entry<String, Integer> entry : options.entrySet()) {
            String prompt = "Want to keep " + entry.getKey() + "(" + getCountForScore(entry.getValue()) + ") ["
                    + entry.getValue() + " points]? (Y/N)";
            out.println(prompt);
            out.flush();

            String response = "";
            if (in.hasNextLine()) {
                response = in.nextLine();
            }

            if (response.equalsIgnoreCase("Y")) {
                return entry.getKey();
            }
        }
        return null;
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
