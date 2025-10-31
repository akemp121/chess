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

import java.util.ArrayList;

public class ListGamesServiceTests {

    @Test
    @DisplayName("List Games Successful")
    public void listGamesSuccessful() {

        try {

            Service service = new Service();

            // Register

            RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
            RegisterResponse res = service.register(req);

            ArrayList<ListGameData> tempGameData = new ArrayList<>();

            // Create game 1

            CreateGameRequest cGameReq1 = new CreateGameRequest(res.authToken(), "b'atzunk");
            CreateGameResponse cGameRes1 = service.createGame(cGameReq1);
            ListGameData gd1 = new ListGameData(cGameRes1.gameID(), null, null, "b'atzunk");
            tempGameData.add(gd1);

            // Create game 2

            CreateGameRequest cGameReq2 = new CreateGameRequest(res.authToken(), "li pleet");
            CreateGameResponse cGameRes2 = service.createGame(cGameReq2);
            ListGameData gd2 = new ListGameData(cGameRes2.gameID(), null, null, "li pleet");
            tempGameData.add(gd2);

            // List games

            ListGamesRequest req2 = new ListGamesRequest(res.authToken());
            ListGamesResponse res2 = service.listGames(req2);

            Assertions.assertEquals(tempGameData, res2.games(),
                    "Games weren't added properly!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Unauthorized List Games")
    public void unauthorizedListGames() {

        try {

            Service service = new Service();

            // Register

            RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
            RegisterResponse res = service.register(req);
            LogoutRequest lr = new LogoutRequest(res.authToken());
            service.logout(lr);

            // List games

            ListGamesRequest req2 = new ListGamesRequest(res.authToken());

            Assertions.assertThrows(UnauthorizedException.class, () -> {
                service.listGames(req2);
            }, "Games were listed when user was unauthorized!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

}
