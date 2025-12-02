package server.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class WebSocketSessions {

    public final ConcurrentHashMap<Integer, Set<Session>> sessions = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        sessions.get(gameID).add(session);
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        sessions.get(gameID).remove(session);
    }

    public Set<Session> getSessionsForGame(Integer gameID) {
        return sessions.get(gameID);
    }

    public void broadcast(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = message.toString();
        for (Session s : sessions.get(gameID)) {
            if (s.isOpen()) {
                if (!s.equals(excludeSession)) {
                    s.getRemote().sendString(msg);
                }
            }
        }
    }

}
