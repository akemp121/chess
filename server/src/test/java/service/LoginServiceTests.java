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

public class LoginServiceTests {

    @Test
    @DisplayName("Login Successful")
    public void loginSuccessful() {

        Service service = new Service();

        // Test register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        service.register(req);

        // Logging in

        LoginRequest lReq = new LoginRequest("q'em", "ha");
        LoginResponse response = service.login(lReq);
        AuthData aData = service.getAuthDAO().getAuth(response.authToken());
        Assertions.assertEquals(response.authToken(), aData.authToken(),
                "authToken not created!");

    }

    @Test
    @DisplayName("Incorrect Password")
    public void incorrectPassword() {

        Service service = new Service();

        // Test register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        service.register(req);

        // Logging in

        LoginRequest lReq = new LoginRequest("q'em", "q'eq");
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.login(lReq);
        }, "Incorrect password entered, exception not thrown!");

    }

    @Test
    @DisplayName("User doesn't exist")
    public void userNoExist() {

        Service service = new Service();

        // Logging in

        LoginRequest lReq = new LoginRequest("q'em", "ha");
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.login(lReq);
        }, "User didn't exist, but exception wasn't thrown!");

    }

}
