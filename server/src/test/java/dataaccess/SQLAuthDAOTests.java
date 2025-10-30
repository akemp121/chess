package dataaccess;

import chess.ChessGame;
import dataaccess.sql.*;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.*;

import requests.*;
import responses.*;
import dataaccess.*;
import model.*;

public class SQLAuthDAOTests {

    @Test
    @DisplayName("Create Auth Successful")
    public void createAuthSuccessful() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        AuthData ad = authDAO.createAuth("q'em ha");

        Assertions.assertNotNull(ad, "Auth not created!");

        AuthData retrievedAD = authDAO.getAuth(ad.authToken());

        String retrievedName = retrievedAD.username();

        String retrievedAuthToken = retrievedAD.authToken();

        Assertions.assertEquals(ad.authToken(), retrievedAuthToken,
                "Created and retrieved authTokens don't match!");
        Assertions.assertEquals(ad.username(), retrievedName,
                "Created and retrieved usernames don't match!");

    }

    @Test
    @DisplayName("Create Auth Empty Username")
    public void createAuthEmptyUsername() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.createAuth(null);
        }, "Auth created with empty username");

    }

    @Test
    @DisplayName("Get Auth Successful")
    public void getAuthSuccessful() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        AuthData ad = authDAO.createAuth("chaab'il winq");

        AuthData retrievedAD = authDAO.getAuth(ad.authToken());

        String retrievedName = retrievedAD.username();

        String retrievedAuthToken = retrievedAD.authToken();

        Assertions.assertEquals(ad.authToken(), retrievedAuthToken,
                "Created and retrieved authTokens don't match!");
        Assertions.assertEquals(ad.username(), retrievedName,
                "Created and retrieved usernames don't match!");

    }

    @Test
    @DisplayName("Get Nonexistent Auth")
    public void getNonexistentAuth() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        AuthData ad = authDAO.createAuth("maa'us aj winq");

        authDAO.clear();

        AuthData retrievedAD = authDAO.getAuth(ad.authToken());

        Assertions.assertNull(retrievedAD);

    }




}
