package ui;

import exception.ResponseException;
import server.ServerFacade;
import ui.States;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private States state = States.LOGGED_OUT;

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_GREEN + "Welcome to 240 Chess!");
        System.out.println(SET_TEXT_COLOR_GREEN + help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
        }
    }

    private void printPrompt() {
        System.out.println("\n" + SET_TEXT_COLOR_GREEN + ">>> ");
    }

    private String evaluate(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd;
            if (!tokens[0].isEmpty()) {
                cmd = tokens[0];
            } else {
                cmd = "help";
            }
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);

        } catch (Exception e) {
            return e.getMessage();
        }
        return "stuff";
    }

    private String help() {
        if (state == States.LOGGED_OUT) {
            return """
                    These are the commands you can perform:
                    - help
                    - quit
                    - login <username> <password>
                    - register <username> <password> <email>
                    """;
        }
        return """
                These are the commands you can perform:
                - help
                - logout
                - create_game <game_name>
                - list_games
                - play_game <game_id> <white/black>
                - observe_game <game_id>
                """;
    }
}





















