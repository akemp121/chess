package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

        // clear
        javalin.delete("/db", ctx -> ctx.result("Clear not implemented"));

        // register
        javalin.post("/user", ctx -> ctx.result("Register not implemented"));

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
