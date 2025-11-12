package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import model.*;
import requests.*;
import responses.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:%d", port));
        try {
            facade.clear();
        } catch (ResponseException e) {
            Assertions.fail("Database failed to clear!");
        }

    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @DisplayName("Register Successful")
    public void registerSuccessful() {

        RegisterRequest request = new RegisterRequest("player2", "password", "p1@email.com");

        try {
            var authData = facade.register(request);
            Assertions.assertTrue(authData.authToken().length() > 10);
        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Register Fail")
    public void registerFail() {

        RegisterRequest request = new RegisterRequest(null, null, null);

        Assertions.assertThrows(ResponseException.class, () -> {
            facade.register(request);
        }, "Registered user without any data!");

    }



}
