import chess.*;
import ui.ChessClient;

public class Main {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        try {
            new ChessClient(serverUrl);
        } catch (Throwable e) {
            System.out.printf("Unable to start server: %s%n", e.getMessage());
        }

    }
}