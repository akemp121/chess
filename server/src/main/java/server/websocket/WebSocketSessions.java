package server.websocket;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.Session;


public class WebSocketSessions {

    public final ConcurrentHashMap<Integer, Set<Session>> sessionMap = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        sessionMap.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet())
                .add(session);
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        sessionMap.get(gameID).remove(session);
    }

}
