package services;

import dataaccess.memory.*;
import dataaccess.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import requests.*;
import responses.*;
import dataaccess.*;
import model.*;

import java.util.ArrayList;

public class Service {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public Service() throws DataAccessException {
        this.authDAO = new SQLAuthDAO();
        this.gameDAO = new MemoryGameDAO();
        this.userDAO = new SQLUserDAO();
    }

    public AuthDAO getAuthDAO() {
        return authDAO;
    }

    public GameDAO getGameDAO() {
        return gameDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    public RegisterResponse register(RegisterRequest request) throws AlreadyTakenException, BadRequest, DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new BadRequest("Error: Missing arguments!");
        }
        UserData uData = new UserData(request.username(), request.password(), request.email());
        UserData existingData = userDAO.getUser(uData.username());
        if (existingData != null) {
            throw new AlreadyTakenException("Error: Username already taken!");
        }
        userDAO.createUser(uData);
        AuthData aData = authDAO.createAuth(uData.username());
        return new RegisterResponse(aData.username(), aData.authToken());
    }

    public LoginResponse login(LoginRequest request) throws BadRequest, UnauthorizedException, DataAccessException {
        if (request.password() == null || request.username() == null) {
            throw new BadRequest("Error: Username or password not given!");
        }
        UserData uData = userDAO.getUser(request.username());
        if (uData == null) {
            throw new UnauthorizedException("Error: User not found!");
        } else if (!BCrypt.checkpw(request.password(), uData.password())) {
            throw new UnauthorizedException("Error: Password incorrect!");
        }
        AuthData aData = authDAO.createAuth(uData.username());
        return new LoginResponse(aData.username(), aData.authToken());
    }

    public LogoutResponse logout(LogoutRequest request) throws UnauthorizedException, DataAccessException {
        AuthData aData = authDAO.getAuth(request.authToken());
        if (aData == null) {
            throw new UnauthorizedException("Error: authToken not found!");
        }
        authDAO.deleteAuth(request.authToken());
        return new LogoutResponse();
    }

    public CreateGameResponse createGame(CreateGameRequest request) throws UnauthorizedException, BadRequest, DataAccessException {
        if (request.gameName() == null) {
            throw new BadRequest("Error: gameName cannot be null!");
        }
        AuthData aData = authDAO.getAuth(request.authToken());
        if (aData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gd = gameDAO.createGame(request.gameName());
        return new CreateGameResponse(gd.gameID());
    }

    public ListGamesResponse listGames(ListGamesRequest request) throws UnauthorizedException, DataAccessException {
        AuthData aData = authDAO.getAuth(request.authToken());
        if (aData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        ArrayList<ListGameData> listOfGames = gameDAO.listGames();
        return new ListGamesResponse(listOfGames);
    }

    public JoinGameResponse joinGame(JoinGameRequest request) throws UnauthorizedException, AlreadyTakenException,
            BadRequest, DataAccessException {
        if (request.gameID() == null || request.playerColor() == null ||
                (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK"))) {
            throw new BadRequest("Error: Teamcolor and gameID cannot be null!");
        }
        AuthData aData = authDAO.getAuth(request.authToken());
        if (aData == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        GameData gData = gameDAO.getGame(request.gameID());
        if (gData == null) {
            throw new BadRequest("Error: Game doesn't exist");
        }
        if ((request.playerColor().equals("WHITE") && gData.whiteUsername() != null) ||
                (request.playerColor().equals("BLACK") && gData.blackUsername() != null)) {
            throw new AlreadyTakenException("Error: Requested team color is already taken!");
        }
        String username = authDAO.getAuth(request.authToken()).username();
        if (request.playerColor().equals("WHITE")) {
            gData = new GameData(gData.gameID(), username, gData.blackUsername(),
                    gData.gameName(), gData.game());
        } else {
            gData = new GameData(gData.gameID(), gData.whiteUsername(), username,
                    gData.gameName(), gData.game());
        }
        gameDAO.updateGame(gData);
        return new JoinGameResponse();
    }

    public ClearResponse clear() throws DataAccessException {
        gameDAO.clear();
        authDAO.clear();
        userDAO.clear();
        return new ClearResponse();
    }
}
