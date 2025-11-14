package ui;

import exception.ResponseException;
import requests.*;
import responses.*;
import server.ServerFacade;
import ui.States;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient {

    private final ServerFacade server;
    private States state = States.LOGGED_OUT;
    private String authToken;

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
            try {
                result = evaluate(line);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
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
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResponse reg = server.register(new RegisterRequest(params[0], params[1], params[2]));
            authToken = reg.authToken();
            state = States.LOGGED_IN;
            return String.format("Registered and logged in as %s!", params[0]);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    private String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResponse log = server.login(new LoginRequest(params[0], params[1]));
            authToken = log.authToken();
            state = States.LOGGED_IN;
            return String.format("Logged in as %s!", params[0]);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    private String logout() throws ResponseException {
        server.logout(new LogoutRequest(authToken));
        state = States.LOGGED_OUT;
        return "Logged out!";
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





















