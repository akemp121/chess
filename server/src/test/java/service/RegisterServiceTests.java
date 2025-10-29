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

public class RegisterServiceTests {

    @Test
    @DisplayName("Add Successful")
    public void addSuccessful() throws DataAccessException {

        Service service = new Service();

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        UserData ud = new UserData("q'em", "ha", "qemha@gmail.com");
        service.register(req);

        Assertions.assertEquals(ud, service.getUserDAO().getUser(ud.username()),
                "User not found in database. User not added");

    }

    @Test
    @DisplayName("User already exists")
    public void userExists() throws DataAccessException {

        Service service = new Service();

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        service.register(req);

        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.register(req);
        }, "AlreadyTakenException not thrown!");


    }

}
