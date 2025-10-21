package server;

import io.javalin.*;
import handlers.Handler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        Handler handler = new Handler();

        // clear
        javalin.delete("/db", ctx -> ctx.result("Clear not implemented"));

        // register
        javalin.post("/user", handler::registerHandler);

        // login
        javalin.post("/session", handler::loginHandler);

        // logout
        javalin.delete("/session", handler::logoutHandler);

        // list games
        javalin.get("/game", ctx -> ctx.result("List games not implemented"));

        // create game
        javalin.post("/game", ctx -> ctx.result("Create game not implemented"));

        // join game
        javalin.put("/game", ctx -> ctx.result("Join game not implemented"));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
