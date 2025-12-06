package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.*;
import websocket.messages.*;
import model.*;
import dataaccess.*;

import java.io.IOException;
import java.util.Collection;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    // do we want DAOs in here or in a service like the server?

    private final WebSocketSessions sessions = new WebSocketSessions();
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public WebSocketHandler() {
        try {
            this.authDAO = new SQLAuthDAO();
            this.gameDAO = new SQLGameDAO();
            this.userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void handleClose(@NotNull WsCloseContext wsCloseContext) throws Exception {
        System.out.println("Disconnected from chess game!");
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext wsConnectContext) {
        System.out.println("Connected to chess game!");
        wsConnectContext.enableAutomaticPings();
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext wsMessageContext) throws Exception {

        // determine which method to run

        try {
            UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
            switch (command.getCommandType()) {

                case CONNECT:
                    UserConnectCommand userConnectCommand = new Gson().fromJson(wsMessageContext.message(), UserConnectCommand.class);
                    connect(userConnectCommand, wsMessageContext.session);
                    break;
                case LEAVE:
                    leave(command, wsMessageContext.session);
                    break;
                case MAKE_MOVE:
                    MakeMoveCommand makeMoveCommand = new Gson().fromJson(wsMessageContext.message(), MakeMoveCommand.class);
                    makeMove(makeMoveCommand, wsMessageContext.session);
                    break;
                case RESIGN:
                    resign(command, wsMessageContext.session);
                    break;
                case REDRAW:
                    updateBoard(command, wsMessageContext.session);
                    break;

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void connect(UserConnectCommand command, Session session) throws IOException, DataAccessException {

        // add user to session and tell everyone

        sessions.addSessionToGame(command.getGameID(), session);
        AuthData ad = authDAO.getAuth(command.getAuthToken());
        if (ad != null) {
            var message = "";
            if (command.getConnectionType() == UserConnectCommand.ConnectionType.PLAYER) {
                message = String.format("%s has joined the game as %s!", ad.username(), command.getTeamColor().toString());

            } else {
                message = String.format("%s is now observing the game!", ad.username());
            }
            broadcastNoti(command.getGameID(), session, message);
            updateBoard(command, session);
        } else {
            var invalidMsg = "Error: Invalid authToken!";
            sendError(invalidMsg, session);
        }


    }

    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {

        // remove user from session and tell everyone else

        sessions.removeSessionFromGame(command.getGameID(), session);
        AuthData ad = authDAO.getAuth(command.getAuthToken());
        GameData gd = gameDAO.getGame(command.getGameID());

        // remove user from game in the db too:


        if (ad.username().equals(gd.whiteUsername())) {

            // if the user was white:

            gameDAO.updateGame(new GameData(gd.gameID(), null, gd.blackUsername(), gd.gameName(), gd.game()));
        } else {

            // if the user was black:

            gameDAO.updateGame(new GameData(gd.gameID(), gd.whiteUsername(), null, gd.gameName(), gd.game()));
        }

        var message = String.format("%s has left the game!", ad.username());
        broadcastNoti(command.getGameID(), session, message);

    }

    private String getContextMessage(ChessGame game) {
        return switch (game.getState()) {
            case RESIGNED -> "Error: A player has resigned from the game! No more moves allowed!";
            case CHECKMATE -> "Error: Game is in checkmate!";
            case STALEMATE -> "Error: Game is in stalemate!";
            default -> null;
        };
    }

    private String moveToString(ChessMove move, String userName) {

        // Gets the chess move and makes it a lil more readable:

        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String sCol = letters[move.getStartPosition().getColumn() - 1];
        String eCol = letters[move.getEndPosition().getColumn() - 1];
        Integer sRow = move.getStartPosition().getRow() - 1;
        Integer eRow = move.getEndPosition().getRow() - 1;
        return String.format("Player %s moved from %s%d to %s%d", userName, sCol, sRow, eCol, eRow);

    }

    private void gameCheck(ChessGame game, Integer gameID) throws IOException {

        // Check if the state of the game has changed
        // If it has, then we need to update the state and notify EVERYONE

        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            var context = "White is in checkmate!";
            broadcastNoti(gameID, null, context);
            game.setState(ChessGame.GameState.CHECKMATE);
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            var context = "Black is in checkmate!";
            broadcastNoti(gameID, null, context);
            game.setState(ChessGame.GameState.CHECKMATE);
        } else if (game.isInStalemate(ChessGame.TeamColor.WHITE)) {
            var context = "White is in stalemate!";
            broadcastNoti(gameID, null, context);
            game.setState(ChessGame.GameState.STALEMATE);
        } else if (game.isInStalemate(ChessGame.TeamColor.BLACK)) {
            var context = "Black is in stalemate!";
            broadcastNoti(gameID, null, context);
            game.setState(ChessGame.GameState.STALEMATE);
        } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            var context = "White is in check!";
            broadcastNoti(gameID, null, context);
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            var context = "Black is in check!";
            broadcastNoti(gameID, null, context);
        }

    }

    private void broadcastNoti(Integer gameID, Session excludeSession, String message) throws IOException {

        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(gameID, excludeSession, notification);

    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException, DataAccessException {

        // Check if the current game is active. If not, then send an error message:

        GameData gd = gameDAO.getGame(command.getGameID());
        AuthData ad = authDAO.getAuth(command.getAuthToken());

        if (gd != null && ad != null) {

            // get team color of the caller:
            ChessGame.TeamColor callerColor;
            if (ad.username().equals(gd.whiteUsername())) {
                callerColor = ChessGame.TeamColor.WHITE;
            } else {
                callerColor = ChessGame.TeamColor.BLACK;
            }

            if (gd.game().getState() != ChessGame.GameState.ACTIVE) {

                sendError(getContextMessage(gd.game()), session);

            } else {

                ChessMove proposedMove = command.getMove();

                // make sure the caller is moving the right team's piece:

                if (gd.game().getBoard().getPiece(proposedMove.getStartPosition()).getTeamColor() == callerColor) {

                    // see if the move is valid

                    try {

                        // if it is, make the move and send the new board to everyone

                        gd.game().makeMove(proposedMove);
                        gameDAO.updateGame(new GameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName(), gd.game()));
                        var newBoard = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gd.game());
                        broadcast(command.getGameID(), null, newBoard);

                        // ALSO BROADCAST WHAT MOVE WAS MADE TO USERS:

                        var moveMade = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, moveToString(proposedMove, ad.username()));
                        broadcast(command.getGameID(), session, moveMade);

                        // ALSO CHECK IF ANYONE IS IN CHECK, CHECKMATE, OR STALEMATE (perhaps in another method?)

                        gameCheck(gd.game(), command.getGameID());

                    } catch (InvalidMoveException e) {

                        // if not, tell only the user that made the move that it was wrong

                        var invalidMsg = "Error: Invalid move!";
                        sendError(invalidMsg, session);

                    }

                } else {

                    var invalidMsg = "Error: Can't move opponent's pieces!";
                    sendError(invalidMsg, session);

                }
            }
        } else {
            var invalidMsg = "Error: authToken or game not found!";
            sendError(invalidMsg, session);
        }
    }

    private void sendError(String context, Session session) throws IOException {
        var error = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, context);
        sendMessage(error, session);
    }

    private void updateBoard(UserGameCommand command, Session session) throws IOException, DataAccessException {
        if (command.getGameID() != null) {
            GameData gd = gameDAO.getGame(command.getGameID());
            if (gd != null) {
                var boardMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gd.game());
                sendMessage(boardMessage, session);
            } else {
                var invalidMsg = "Error: Game not found!";
                sendError(invalidMsg, session);
            }
        }
    }

    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {

        // get the game, set its status to RESIGNED, then send a message to everyone

        AuthData ad = authDAO.getAuth(command.getAuthToken());
        GameData gd = gameDAO.getGame(command.getGameID());
        gd.game().setState(ChessGame.GameState.RESIGNED);
        gameDAO.updateGame(new GameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName(), gd.game()));
        var message = String.format("%s has resigned from the game!", ad.username());
        broadcastNoti(command.getGameID(), session, message);

    }

    private void broadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {

        // send a message to everyone except the excluded session

        String msg = new Gson().toJson(message);
        for (Session s : sessions.sessionMap.get(gameID)) {
            if (s.isOpen()) {
                if (!s.equals(excludeSession)) {
                    s.getRemote().sendString(msg);
                }
            }
        }
    }

    private void sendMessage(ServerMessage message, Session session) throws IOException {

        // send a message to a specific session

        String msg = new Gson().toJson(message);
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}
