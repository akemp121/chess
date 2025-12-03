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
        try {
            UserGameCommand command = new Gson().fromJson(wsMessageContext.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command, wsMessageContext.session);
                case LEAVE -> leave(command, wsMessageContext.session);
                case MAKE_MOVE -> makeMove((MakeMoveCommand) command, wsMessageContext.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException, DataAccessException {
        sessions.addSessionToGame(command.getGameID(), session);
        AuthData ad = authDAO.getAuth(command.getAuthToken());
        var message = String.format("%s has joined the game!", ad.username());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);
        // perhaps code that shows the board to the joiner?
    }

    private void leave(UserGameCommand command, Session session) throws IOException, DataAccessException {
        sessions.removeSessionFromGame(command.getGameID(), session);
        AuthData ad = authDAO.getAuth(command.getAuthToken());
        var message = String.format("%s has left the game!", ad.username());
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);
    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException, DataAccessException {
        GameData gd = gameDAO.getGame(command.getGameID());
        ChessMove proposedMove = command.getMove();
        try {
            gd.game().makeMove(proposedMove);
            gameDAO.updateGame(new GameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName(), gd.game()));
        } catch (InvalidMoveException e) {
            var invalidMsg = "Invalid move!";
            var notification = new ErrorMessage(ServerMessage.ServerMessageType.ERROR, invalidMsg);
            sendMessage(notification, session);
        }
        // perhaps code that sends the new board to everyone in the session?
    }

    private void broadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
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
        String msg = message.toString();
        if (session.isOpen()) {
            session.getRemote().sendString(msg);
        }
    }
}
