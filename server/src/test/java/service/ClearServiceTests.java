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

public class ClearServiceTests {

    @Test
    @DisplayName("Clear Successful")
    public void clearSuccessul() {

        Service service = new Service();

        // Register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        RegisterResponse res = service.register(req);

        // Create game 1

        CreateGameRequest cGameReq1 = new CreateGameRequest(res.authToken(), "b'atzunk");
        service.createGame(cGameReq1);

        // Create game 2

        CreateGameRequest cGameReq2 = new CreateGameRequest(res.authToken(), "li pleet");
        service.createGame(cGameReq2);

        // Clear everything

        ArrayList<ListGameData> empty = new ArrayList<>();

        service.clear();

        Assertions.assertEquals(empty, service.getGameDAO().listGames());
        Assertions.assertNull(service.getAuthDAO().getAuth(res.authToken()));
        Assertions.assertNull(service.getUserDAO().getUser("q'em"));

    }

}
