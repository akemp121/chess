package server.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class WebSocketSessions {

    public final ConcurrentHashMap<Integer, Set<Session>> sessionMap = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        sessionMap.get(gameID).add(session);
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        sessionMap.get(gameID).remove(session);
    }

    public Set<Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }

}
