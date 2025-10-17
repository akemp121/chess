package services;

import dataaccess.memory.*;
import requests.*;
import responses.*;
import dataaccess.*;
import model.*;

public class Service {

    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();
    private final UserDAO userDAO = new MemoryUserDAO();

    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public RegisterResponse register(RegisterRequest request) throws AlreadyTakenException {
        UserData uData = new UserData(request.username(), request.password(), request.email());
        UserData existingData = userDAO.getUser(uData.username());
        if (existingData != null) {
            throw new AlreadyTakenException("Username already taken!");
        }
        userDAO.createUser(uData);
        AuthData aData = authDAO.createAuth(uData.username());
        return new RegisterResponse(aData.username(), aData.authToken());
    }

}
