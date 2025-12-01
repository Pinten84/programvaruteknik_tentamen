package se.lnu.boomerang;

import se.lnu.boomerang.game.BotController;
import se.lnu.boomerang.game.ConsoleController;
import se.lnu.boomerang.game.Game;
import se.lnu.boomerang.game.NetworkPlayerController;
import se.lnu.boomerang.game.PlayerController;
import se.lnu.boomerang.model.Player;
import se.lnu.boomerang.scoring.ScoringStrategy;
import se.lnu.boomerang.scoring.ThrowCatchScoring;
import se.lnu.boomerang.scoring.TouristSiteScoring;
import se.lnu.boomerang.scoring.CollectionScoring;
import se.lnu.boomerang.scoring.AnimalScoring;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 2) {
                int numPlayers = Integer.parseInt(args[0]);
                int numBots = Integer.parseInt(args[1]);

                if (numPlayers + numBots < 2 || numPlayers + numBots > 4) {
                    System.out.println("Total players must be between 2 and 4.");
                    return;
                }

                startServer(numPlayers, numBots);
            } else if (args.length == 1) {
                // Client mode
                String ip = args[0];
                startClient(ip);
            } else {
                System.out.println("Usage:");
                System.out.println("  Server: java -jar boomerang.jar <numPlayers> <numBots>");
                System.out.println("  Client: java -jar boomerang.jar <serverIP>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startServer(int numPlayers, int numBots) {
        List<PlayerController> controllers = new ArrayList<>();

        // Host player (Player 0)
        Player hostPlayer = new Player(0, false);
        ConsoleController hostController = new ConsoleController(new Scanner(System.in),
                new PrintWriter(System.out, true));
        hostController.setPlayer(hostPlayer);
        controllers.add(hostController);

        // Bots
        for (int i = 0; i < numBots; i++) {
            Player botPlayer = new Player(controllers.size(), true);
            BotController botController = new BotController();
            botController.setPlayer(botPlayer);
            controllers.add(botController);
        }

        // Remote players
            // Calculate number of remote players based on input arguments.

        int remotePlayers = numPlayers - 1;
        if (remotePlayers > 0) {
            System.out.println("Waiting for " + remotePlayers + " remote players...");
            for (int i = 0; i < remotePlayers; i++) {
                Player remotePlayer = new Player(controllers.size(), false);
                NetworkPlayerController netController = new NetworkPlayerController();
                netController.setPlayer(remotePlayer);
                controllers.add(netController);
                System.out.println("Remote player connected (Stub).");
            }
        }

        // Initialize Strategies
        List<ScoringStrategy> strategies = new ArrayList<>();
        strategies.add(new ThrowCatchScoring());
        strategies.add(new TouristSiteScoring());
        strategies.add(new CollectionScoring());
        strategies.add(new AnimalScoring());

        Game game = new Game(controllers, strategies);
        game.start();
    }

    private static void startClient(String ip) {
        System.out.println("Client mode not yet implemented.");
    }
}
