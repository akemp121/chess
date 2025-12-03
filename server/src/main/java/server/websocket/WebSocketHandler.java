package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import websocket.commands.*;
import websocket.messages.*;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final WebSocketSessions sessions = new WebSocketSessions();

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
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        sessions.addSessionToGame(command.getGameID(), session);
        // how do we know who the user is?
        var message = "Someone has joined the game!";
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);
    }

    private void leave(UserGameCommand command, Session session) throws IOException {
        sessions.removeSessionFromGame(command.getGameID(), session);
        var message = "Someone has left the game!";
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        broadcast(command.getGameID(), session, notification);
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
