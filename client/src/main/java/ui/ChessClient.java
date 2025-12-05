package ui;

import chess.ChessBoard;
import chess.ChessGame;
import exception.ResponseException;
import server.ServerFacade;
import requests.*;
import responses.*;

import java.util.ArrayList;
import java.util.Arrays;
import model.*;
import websocket.GameHandler;
import websocket.WebSocketFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ChessClient implements GameHandler {

    private final ServerFacade server;
    private final WebSocketFacade ws;
    private States state = States.LOGGED_OUT;
    private String authToken;
    private ChessGame.TeamColor currentColor;
    private Integer currentGame;
    private ArrayList<ListGameData> gameList = new ArrayList<ListGameData>();

    public ChessClient(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        ws = new WebSocketFacade(serverUrl);
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
        System.out.print("\n" + SET_TEXT_COLOR_GREEN + ">>> ");
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
                case "create_game" -> createGame(params);
                case "list_games" -> listGames();
                case "play_game" -> playGame(params);
                case "observe_game" -> observeGame(params);
                case "leave" -> leave();
                case "quit" -> "quit";
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

    private String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            server.createGame(new CreateGameRequest(authToken, params[0]));
            return String.format("Created game %s!", params[0]);
        }
        throw new ResponseException(400, "Expected: <game_name>");
    }

    private String listGames() throws ResponseException {
        ListGamesResponse lgr = server.listGames(new ListGamesRequest(authToken));
        ArrayList<ListGameData> games = lgr.games();
        StringBuilder sb = new StringBuilder();
        sb.append("Available Games: \n");
        if (games == null || games.isEmpty()) {
            sb.append("- no active games");
            return sb.toString();
        }
        for (int i = 0; i < games.size(); i++) {
            sb.append(String.format("- %d: %s, Black: %s, White: %s\n", i + 1,
                    games.get(i).gameName(), games.get(i).blackUsername(), games.get(i).whiteUsername()));
        }
        gameList.clear();
        gameList.addAll(games);
        return sb.toString();
    }

    private String playGame(String... params) throws ResponseException {
        if (params.length == 2) {
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
                if (gameNumber <= 0) {
                    throw new ResponseException(400, "Game id must be a valid integer!");
                } else if (gameNumber > gameList.size()) {
                    throw new ResponseException(400, "Game doesn't exist!");
                }
            } catch (Exception e) {
                throw new ResponseException(400, "Game id must be a valid integer!");
            }
            server.joinGame(new JoinGameRequest(authToken, params[1].toUpperCase(), gameList.get(gameNumber - 1).gameID()));
            state = States.GAMEPLAY;
            setCurrentColor(params[1]);
            currentGame = gameList.get(gameNumber - 1).gameID();
            ws.joinGame(authToken, gameList.get(gameNumber - 1).gameID(), currentColor);
            return String.format("Joined game %s as color %s!", gameList.get(gameNumber - 1).gameName(), params[1]);
        }
        throw new ResponseException(400, "Expected: <game_id> <white/black>");
    }

    private void setCurrentColor(String color) {
        if (color.equalsIgnoreCase("black")) {
            this.currentColor = ChessGame.TeamColor.BLACK;
        } else {
            this.currentColor = ChessGame.TeamColor.WHITE;
        }
    }

    private String observeGame(String... params) throws ResponseException {
        if (params.length == 1) {
            int gameNumber;
            try {
                gameNumber = Integer.parseInt(params[0]);
                if (gameNumber <= 0) {
                    throw new ResponseException(400, "Game id must be a valid integer!");
                }
            } catch (Exception e) {
                throw new ResponseException(400, "Game id must be a valid integer!");
            }
            state = States.GAMEPLAY;
            setCurrentColor("WHITE");
            currentGame = gameList.get(gameNumber - 1).gameID();
            ws.observeGame(authToken, gameList.get(gameNumber - 1).gameID());
            return String.format("Observing game %s!", gameList.get(gameNumber - 1).gameName());
        }
        throw new ResponseException(400, "Expected: <game_id>");
    }

    private String leave() {
        ws.leaveGame(authToken, currentGame);
        state = States.LOGGED_IN;
        return "Left game!";
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
        } else if (state == States.LOGGED_IN) {
            return """
                These are the commands you can perform:
                - help
                - logout
                - create_game <game_name>
                - list_games
                - play_game <game_id> <white/black>
                - observe_game <game_id>
                """;
        } else if (state == States.GAMEPLAY) {
            return """
                These are the commands you can perform:
                - help
                - redraw_board
                - leave
                - make_move <starting_position> <ending_position>
                - resign
                - highlight_moves <position>
                """;
        } else {
            return "Error: No state selected";
        }

    }

    @Override
    public void updateGame(ChessGame game) {
        BoardIllustrator.illustrate(game.getBoard(), currentColor);
    }

    @Override
    public void printMessage(String message) {
        System.out.println(SET_TEXT_COLOR_GREEN + message);
        printPrompt();
    }
}





















