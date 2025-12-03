package server;

import io.javalin.*;
import handlers.Handler;
import server.websocket.WebSocketHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        Handler handler = new Handler();

        WebSocketHandler wsHandler = new WebSocketHandler();

        // clear
        javalin.delete("/db", handler::clearHandler);

        // register
        javalin.post("/user", handler::registerHandler);

        // login
        javalin.post("/session", handler::loginHandler);

        // logout
        javalin.delete("/session", handler::logoutHandler);

        // list games
        javalin.get("/game", handler::listGamesHandler);

        // create game
        javalin.post("/game", handler::createGameHandler);

        // join game
        javalin.put("/game", handler::joinGameHandler);

        // websocket
        javalin.ws("/ws", ws -> {
            ws.onConnect(wsHandler);
            ws.onClose(wsHandler);
            ws.onMessage(wsHandler);
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
