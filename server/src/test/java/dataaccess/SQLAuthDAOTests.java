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
    public void createAuthSuccessful() {

        try {

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

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Create Auth Empty Username")
    public void createAuthEmptyUsername() {

        try {

            AuthDAO authDAO = new SQLAuthDAO();

            Assertions.assertThrows(DataAccessException.class, () -> {
                authDAO.createAuth(null);
            }, "Auth created with empty username");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Get Auth Successful")
    public void getAuthSuccessful() {

        try {

            AuthDAO authDAO = new SQLAuthDAO();

            AuthData ad = authDAO.createAuth("chaab'il winq");

            AuthData retrievedAD = authDAO.getAuth(ad.authToken());

            String retrievedName = retrievedAD.username();

            String retrievedAuthToken = retrievedAD.authToken();

            Assertions.assertEquals(ad.authToken(), retrievedAuthToken,
                    "Created and retrieved authTokens don't match!");
            Assertions.assertEquals(ad.username(), retrievedName,
                    "Created and retrieved usernames don't match!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Get Nonexistent Auth")
    public void getNonexistentAuth() {

        try {

            AuthDAO authDAO = new SQLAuthDAO();

            AuthData ad = authDAO.createAuth("maa'us aj winq");

            authDAO.clear();

            AuthData retrievedAD = authDAO.getAuth(ad.authToken());

            Assertions.assertNull(retrievedAD,
                    "AuthToken was returned when there wasn't one to begin with!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Delete Auth Successful")
    public void deleteAuthSuccessful() {

        try {

            AuthDAO authDAO = new SQLAuthDAO();

            AuthData ad = authDAO.createAuth("winq mas yib' iru");

            authDAO.deleteAuth(ad.authToken());

            Assertions.assertNull(authDAO.getAuth(ad.authToken()),
                    "AuthData not deleted!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("Delete Auth Without Auth")
    public void deleteAuthWithoutAuth() {

        try {

            AuthDAO authDAO = new SQLAuthDAO();

            Assertions.assertDoesNotThrow(() -> {
                        authDAO.deleteAuth(null);
                    }, "Delete failed!"
            );

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }




    }


    @Test
    @DisplayName("Clear Successful")
    public void clearSuccessful() {

        try {

            AuthDAO authDAO = new SQLAuthDAO();

            AuthData ad = authDAO.createAuth("chaab'il winq");

            AuthData ad2 = authDAO.createAuth("maa'us aj winq");

            authDAO.clear();

            AuthData retrievedAD = authDAO.getAuth(ad.authToken());

            AuthData retrievedAD2 = authDAO.getAuth(ad2.authToken());

            Assertions.assertNull(retrievedAD, "First authToken not cleared!");
            Assertions.assertNull(retrievedAD2, "Second authToken not cleared!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


}
