package service;

import dataaccess.memory.*;
import requests.*;
import responses.*;
import dataaccess.*;
import model.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.Service;


public class JoinGameServiceTests {

    @Test
    @DisplayName("Join Game Successful")
    public void joinGameSuccessul() throws DataAccessException {

        Service service = new Service();

        // Register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        RegisterResponse res = service.register(req);

        RegisterRequest req1 = new RegisterRequest("winq", "cuc", "cuc@gmail.com");
        RegisterResponse res1 = service.register(req1);

        // Create game 1

        CreateGameRequest cGameReq = new CreateGameRequest(res.authToken(), "b'atzunk");
        CreateGameResponse cGameRes = service.createGame(cGameReq);

        // Join Game

        JoinGameRequest jGameReq1 = new JoinGameRequest(res.authToken(), "WHITE", cGameRes.gameID());
        JoinGameRequest jGameReq2 = new JoinGameRequest(res1.authToken(), "BLACK", cGameRes.gameID());

        service.joinGame(jGameReq1);
        service.joinGame(jGameReq2);

        Assertions.assertEquals("q'em", service.getGameDAO().getGame(cGameRes.gameID()).whiteUsername(),
                "White not added correctly!");
        Assertions.assertEquals("winq", service.getGameDAO().getGame(cGameRes.gameID()).blackUsername(),
                "Black not added correctly!");


    }

    @Test
    @DisplayName("Spot Taken")
    public void spotTaken() throws DataAccessException {

        Service service = new Service();

        // Register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        RegisterResponse res = service.register(req);

        RegisterRequest req1 = new RegisterRequest("winq", "cuc", "cuc@gmail.com");
        RegisterResponse res1 = service.register(req1);

        // Create game 1

        CreateGameRequest cGameReq = new CreateGameRequest(res.authToken(), "b'atzunk");
        CreateGameResponse cGameRes = service.createGame(cGameReq);

        // Join Game

        JoinGameRequest jGameReq1 = new JoinGameRequest(res.authToken(), "WHITE", cGameRes.gameID());
        JoinGameRequest jGameReq2 = new JoinGameRequest(res1.authToken(), "WHITE", cGameRes.gameID());

        service.joinGame(jGameReq1);

        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.joinGame(jGameReq2);
        }, "Second player requested a taken color and no exception was thrown!");

    }

    @Test
    @DisplayName("Nonexistent Game")
    public void nonexistentGame() throws DataAccessException {

        Service service = new Service();

        // Register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        RegisterResponse res = service.register(req);

        // Join Game

        JoinGameRequest jGameReq1 = new JoinGameRequest(res.authToken(), "WHITE", 2112);

        Assertions.assertThrows(BadRequest.class, () -> {
            service.joinGame(jGameReq1);
        }, "User tried to join nonexistent game and no exception was thrown!");

    }

}
