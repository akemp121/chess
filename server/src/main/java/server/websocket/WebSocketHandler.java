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
                case CONNECT -> connect(command, wsMessageContext.session);
                case LEAVE -> leave(command, wsMessageContext.session);
                case MAKE_MOVE -> makeMove((MakeMoveCommand) command, wsMessageContext.session);
                case RESIGN -> resign(command, wsMessageContext.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void connect(UserGameCommand command, Session session) throws IOException, DataAccessException {

        // add user to session and tell everyone

        sessions.addSessionToGame(command.getGameID(), session);
        AuthData ad = authDAO.getAuth(command.getAuthToken());
        var message = String.format("%s has joined the game!", ad.username());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);

        // send the board to the user

        GameData gd = gameDAO.getGame(command.getGameID());
        var boardMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gd.game());
        sendMessage(boardMessage, session);

    }

    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {

        // remove user from session and tell everyone else

        sessions.removeSessionFromGame(command.getGameID(), session);

        // remove user from game in the db too:

        AuthData ad = authDAO.getAuth(command.getAuthToken());
        var message = String.format("%s has left the game!", ad.username());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);

    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException, DataAccessException {

        // get the current game and proposed move

        GameData gd = gameDAO.getGame(command.getGameID());
        ChessMove proposedMove = command.getMove();

        // see if the move is valid

        try {

            // if it is, make the move and send the new board to everyone

            gd.game().makeMove(proposedMove);
            gameDAO.updateGame(new GameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName(), gd.game()));
            var message = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME, gd.game());
            broadcast(command.getGameID(), null, message);

            // ALSO BROADCAST WHAT MOVE WAS MADE TO USERS:
            // ALSO CHECK IF ANYONE IS IN CHECK, CHECKMATE, OR STALEMATE (perhaps in another method?)

        } catch (InvalidMoveException e) {

            // if not, tell only the user that made the move that it was wrong

            var invalidMsg = "Error: Invalid move!";
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, invalidMsg);
            sendMessage(notification, session);

        }
    }

    private void resign(UserGameCommand command, Session session) throws IOException, DataAccessException {

        // get the game, set its status to RESIGNED, then send a message to everyone

        AuthData ad = authDAO.getAuth(command.getAuthToken());
        GameData gd = gameDAO.getGame(command.getGameID());
        gd.game().setState(ChessGame.GameState.RESIGNED);
        gameDAO.updateGame(new GameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName(), gd.game()));
        var message = String.format("%s has resigned from the game!", ad.username());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);

    }

    private void broadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {

        // send a message to everyone except the excluded session

        String msg = message.toString();
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

        String msg = message.toString();
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}
