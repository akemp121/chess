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

        Assertions.assertNull(retrievedAD,
                "AuthToken was returned when there wasn't one to begin with!");

    }

    @Test
    @DisplayName("Delete Auth Successful")
    public void deleteAuthSuccessful() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        AuthData ad = authDAO.createAuth("winq mas yib' iru");

        authDAO.deleteAuth(ad.authToken());

        Assertions.assertNull(authDAO.getAuth(ad.authToken()),
                "AuthData not deleted!");

    }

    // I don't think this is useful:

    /*
    @Test
    @DisplayName("Delete Auth Without Auth")
    public void deleteAuthWithoutAuth() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.deleteAuth(null);
        }, "Tried to delete auth without authToken and no exception raised");

    }
    */

    @Test
    @DisplayName("Clear Successful")
    public void clearSuccessful() throws DataAccessException {

        AuthDAO authDAO = new SQLAuthDAO();

        AuthData ad = authDAO.createAuth("chaab'il winq");

        AuthData ad2 = authDAO.createAuth("maa'us aj winq");

        authDAO.clear();

        AuthData retrievedAD = authDAO.getAuth(ad.authToken());

        AuthData retrievedAD2 = authDAO.getAuth(ad2.authToken());

        Assertions.assertNull(retrievedAD, "First authToken not cleared!");
        Assertions.assertNull(retrievedAD2, "Second authToken not cleared!");

    }


}
