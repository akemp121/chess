package client;

import exception.ResponseException;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import model.*;
import requests.*;
import responses.*;

import java.util.ArrayList;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:%d", port));
    }

    @BeforeAll
    public static void reset() {
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

        RegisterRequest request = new RegisterRequest("q'em ha", "massawichik", "qh@email.com");

        try {
            var authData = facade.register(request);
            Assertions.assertTrue(authData.authToken().length() > 10,
                    "No data returned!");
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

    @Test
    @DisplayName("Login Successful")
    public void loginSuccessful() {

        try {

            RegisterRequest request = new RegisterRequest("q'em ha 2", "massawichik", "qh2@email.com");

            facade.register(request);

            LoginRequest logRequest = new LoginRequest("q'em ha 2", "massawichik");

            var loginData = facade.login(logRequest);

            Assertions.assertTrue(loginData.authToken().length() > 10,
                    "No data returned!");

        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Login Fail")
    public void loginFail() {

        try {

            RegisterRequest request = new RegisterRequest("q'em ha 3", "massawichik", "qh2@email.com");

            facade.register(request);

            LoginRequest logRequest = new LoginRequest(null, "moko us ta");

            Assertions.assertThrows(ResponseException.class, () -> {
                facade.login(logRequest);
            }, "Logged in user without a username!");

        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Logout Successful")
    public void logoutSuccessful() {

        try {

            RegisterRequest request = new RegisterRequest("chaab'il winq", "laainchaab'il", "cw@email.com");

            var loginData = facade.register(request);

            LogoutRequest logoutRequest = new LogoutRequest(loginData.authToken());

            facade.logout(logoutRequest);

        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Logout Fail")
    public void logoutFail() {

        try {

            RegisterRequest request = new RegisterRequest("chaab'il ixq", "laainchaab'il", "ci@email.com");

            facade.register(request);

            LogoutRequest logoutRequest = new LogoutRequest(null);

            Assertions.assertThrows(ResponseException.class, () -> {
                facade.logout(logoutRequest);
            }, "Logged out without authToken!");


        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Create Game Successful")
    public void createGameSuccessful() {

        try {

            RegisterRequest request = new RegisterRequest("yiib'anel b'atzul", "laainchaab'il", "yb@email.com");

            var loginData = facade.register(request);

            CreateGameRequest createGameRequest = new CreateGameRequest(loginData.authToken(), "li nimla pleet");

            var gameData = facade.createGame(createGameRequest);

            Assertions.assertTrue(gameData.gameID() != 0,
                    "gameID not returned!");

        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Create Game Fail")
    public void createGameFail() {

        try {

            RegisterRequest request = new RegisterRequest("yiib' iru winq", "laainchaab'il", "yiw@email.com");

            facade.register(request);

            CreateGameRequest createGameRequest = new CreateGameRequest(null, "li nimla pleet");

            Assertions.assertThrows(ResponseException.class, () -> {
                facade.createGame(createGameRequest);
            }, "Game created without authToken!");

        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("List Games Successful")
    public void listGamesSuccessful() {

        try {

            RegisterRequest request = new RegisterRequest("li nim aj yiib'anel b'atzul", "laainchaab'il", "lnayb@email.com");

            var loginData = facade.register(request);

            CreateGameRequest createGameRequest = new CreateGameRequest(loginData.authToken(), "li xkab'il nimla pleet");

            CreateGameRequest createGameRequest2 = new CreateGameRequest(loginData.authToken(), "li roxil nimla pleet");

            facade.createGame(createGameRequest);

            facade.createGame(createGameRequest2);

            ListGamesRequest listGamesRequest = new ListGamesRequest(loginData.authToken());

            var listOfGames = facade.listGames(listGamesRequest);

            Assertions.assertNotEquals(new ArrayList<>(), listOfGames,
                    "Games not returned!");

        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("List Games Fail")
    public void listGamesFail() {

        try {

            RegisterRequest request = new RegisterRequest("laj yaj", "laainchaab'il", "ly@email.com");

            var loginData = facade.register(request);

            CreateGameRequest createGameRequest = new CreateGameRequest(loginData.authToken(), "li xka'il nimla pleet");

            CreateGameRequest createGameRequest2 = new CreateGameRequest(loginData.authToken(), "li ro'il nimla pleet");

            facade.createGame(createGameRequest);

            facade.createGame(createGameRequest2);

            ListGamesRequest listGamesRequest = new ListGamesRequest(null);

            Assertions.assertThrows(ResponseException.class, () -> {
                facade.listGames(listGamesRequest);
            }, "Tried to list games without authToken!");


        } catch (ResponseException e) {
            Assertions.fail("Unexpected ResponseException: " + e.getMessage());
        }

    }

}
