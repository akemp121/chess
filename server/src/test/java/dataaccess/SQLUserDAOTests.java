package dataaccess;

import dataaccess.sql.*;
import org.junit.jupiter.api.*;

import org.mindrot.jbcrypt.BCrypt;
import model.*;

public class SQLUserDAOTests {

    @Test
    @DisplayName("Create User Successful")
    public void createUserSuccessful() {

        try {

            UserDAO userDAO = new SQLUserDAO();

            UserData createUserData = new UserData("q'em ha'", "massawichik", "qemha@gmail.com");

            userDAO.createUser(createUserData);

            UserData retrievedUD = userDAO.getUser(createUserData.username());

            Assertions.assertNotNull(retrievedUD, "User not found!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Create User Empty Fields")
    public void createUserEmptyFields() {

        try {

            UserDAO userDAO = new SQLUserDAO();

            UserData badUD1 = new UserData(null, null, null);

            Assertions.assertThrows(DataAccessException.class, () -> {
                userDAO.createUser(badUD1);
            }, "User created with empty fields!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Get User Successful")
    public void getUserSuccessful() {

        try {

            UserDAO userDAO = new SQLUserDAO();

            UserData createUserData = new UserData("maa'us aj winq", "massawichik", "qemha@gmail.com");

            userDAO.createUser(createUserData);

            UserData retrievedUD = userDAO.getUser(createUserData.username());

            Assertions.assertEquals(createUserData.username(), retrievedUD.username(),
                    "Created and retrieved usernames don't match!");
            Assertions.assertTrue(BCrypt.checkpw(createUserData.password(), retrievedUD.password()),
                    "Created and retrieved passwords don't match!");
            Assertions.assertEquals(createUserData.email(), retrievedUD.email(),
                    "Created and retrieved emails don't match!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Get Nonexistent User")
    public void getNonexistentUser() {

        try {

            UserDAO userDAO = new SQLUserDAO();

            UserData createUserData = new UserData("china us ixq", "massawichik", "qemha@gmail.com");

            userDAO.clear();

            UserData retrievedUD = userDAO.getUser(createUserData.username());

            Assertions.assertNull(retrievedUD,
                    "UserData was returned when there wasn't any to begin with!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("Clear Successful")
    public void clearSuccessful() {

        try {

            UserDAO userDAO = new SQLUserDAO();

            UserData ud1 = new UserData("china al", "massawichik", "qemha@gmail.com");
            UserData ud2 = new UserData("nimla winq", "laainmaschaabil", "chaabil@gmail.com");

            userDAO.createUser(ud1);
            userDAO.createUser(ud2);

            userDAO.clear();

            UserData retrievedUD1 = userDAO.getUser(ud1.username());

            UserData retrievedUD2 = userDAO.getUser(ud2.username());

            Assertions.assertNull(retrievedUD1, "First user not cleared!");
            Assertions.assertNull(retrievedUD2, "Second user not cleared!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


}
