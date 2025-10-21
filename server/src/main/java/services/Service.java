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

    public LoginResponse login(LoginRequest request) throws BadRequest, UnauthorizedException {
        UserData uData = userDAO.getUser(request.username());
        if (uData == null) {
            throw new BadRequest("User not found!");
        } else if (!uData.password().equals(request.password())) {
            throw new UnauthorizedException("Password incorrect!");
        }
        AuthData aData = authDAO.createAuth(uData.username());
        return new LoginResponse(aData.username(), aData.authToken());
    }

    public LogoutResponse logout(LogoutRequest request) throws UnauthorizedException {
        AuthData aData = authDAO.getAuth(request.authToken());
        if (aData == null) {
            throw new UnauthorizedException("AuthToken not found!");
        }
        authDAO.deleteAuth(request.authToken());
        return new LogoutResponse();
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws UnauthorizedException {
        AuthData aData = authDAO.getAuth(request.authToken());
        if (aData == null) {
            throw new UnauthorizedException("Unauthorized");
        }
        GameData gd = gameDAO.createGame(request.gameName());
        return new CreateGameResponse(gd.gameID());
    }
}
