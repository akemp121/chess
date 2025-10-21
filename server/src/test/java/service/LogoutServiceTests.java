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

public class LogoutServiceTests {

    @Test
    @DisplayName("Logout Successful")
    public void logoutSuccessful() {

        Service service = new Service();

        // Register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        service.register(req);

        // Login

        LoginRequest lReq = new LoginRequest("q'em", "ha");
        LoginResponse lRes = service.login(lReq);

        // Logout

        LogoutRequest loReq = new LogoutRequest(lRes.authToken());
        service.logout(loReq);
        AuthData aData = service.getAuthDAO().getAuth(lRes.authToken());

        Assertions.assertNull(aData, "authToken not deleted!");

    }

    @Test
    @DisplayName("Logout Unauthorized")
    public void logoutUnauthorized() {

        Service service = new Service();

        // Register

        RegisterRequest req = new RegisterRequest("q'em", "ha", "qemha@gmail.com");
        service.register(req);

        // Login

        LoginRequest lReq = new LoginRequest("q'em", "ha");
        LoginResponse lRes = service.login(lReq);

        // Logout

        LogoutRequest loReq = new LogoutRequest(lRes.authToken());
        service.logout(loReq);

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.logout(loReq);
        }, "Tried to delete a non-existent authToken, exception not thrown!");

    }
}
