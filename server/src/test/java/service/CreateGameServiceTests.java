package service;

import chess.ChessGame;
import dataaccess.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.Service;
import requests.*;
import responses.*;

public class CreateGameServiceTests {



    @Test
    @DisplayName("Game Created")
    public void gameCreated() {

        try {

            Service service = new Service();

            // Register

            RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
            RegisterResponse res = service.register(req);

            // Create game

            CreateGameRequest cGameReq = new CreateGameRequest(res.authToken(), "b'atzunk");
            CreateGameResponse cGameRes = service.createGame(cGameReq);

            Assertions.assertEquals(1001, cGameRes.gameID(),
                    "gameID didn't generate as expected!");
            ChessGame example = new ChessGame();
            Assertions.assertEquals(example, service.getGameDAO().getGame(cGameRes.gameID()).game(),
                    "game wasn't created or was created incorrectly!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Unauthorized Creation")
    public void unauthorizedCreation() {

        try {

            Service service = new Service();

            // Register

            RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
            RegisterResponse res = service.register(req);
            LogoutRequest lr = new LogoutRequest(res.authToken());
            service.logout(lr);

            // Create game

            CreateGameRequest cGameReq = new CreateGameRequest(res.authToken(), "b'atzunk");

            Assertions.assertThrows(UnauthorizedException.class, () -> {
                service.createGame(cGameReq);
            }, "Game created when user was unauthorized");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


}
