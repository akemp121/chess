package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.*;
import handlers.Handler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        var handler = new Handler();

        // clear
        javalin.delete("/db", ctx -> ctx.result("Clear not implemented"));

        // register
        javalin.post("/user", handler::registerHandler);

        // login
        javalin.post("/session", ctx -> ctx.result("Login not implemented"));

        // logout
        javalin.delete("/session", ctx -> ctx.result("Logout not implemented"));

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
