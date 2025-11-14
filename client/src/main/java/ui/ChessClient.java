package ui;

import exception.ResponseException;
import server.ServerFacade;
import ui.States;
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





















