package se.lnu.boomerang.game;

import se.lnu.boomerang.model.Card;
import se.lnu.boomerang.model.Player;
import java.util.List;
import java.util.Map;

/**
 * Stub for a Network Player Controller.
 * Demonstrates how the system can be extended to support remote players.
 * In a real implementation, this would wrap a Socket or similar connection.
 */
public class NetworkPlayerController implements PlayerController {
    private Player player;
    // private Socket socket;
    // private ObjectOutputStream out;
    // private ObjectInputStream in;

    public NetworkPlayerController() {
        // Initialize connection
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
        // Send message over network
        // out.writeObject(message);
        System.out.println("[Network Stub] Sending to player " + player.getId() + ": " + message);
    }

    @Override
    public String requestInput(String prompt) {
        // Send prompt and wait for response
        // out.writeObject(prompt);
        // return (String) in.readObject();
        return "";
    }

    @Override
    public Card selectCardToDraft(List<Card> hand) {
        // Send hand to client, wait for card selection
        return null;
    }

    @Override
    public Card selectThrowCard(List<Card> hand) {
        // Send hand to client, wait for throw card selection
        return null;
    }

    @Override
    public String selectActivityToScore(Map<String, Integer> options) {
        // Send options to client, wait for selection
        return null;
    }
}
