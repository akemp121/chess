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

public class SQLGameDAOTests {

    @Test
    @DisplayName("Create Game Successful")
    public void createGameSuccessful() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd = gameDAO.createGame("li nimla pleetik");

            Assertions.assertNotNull(gd, "Game not created!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Create Game Empty Name")
    public void createGameEmptyName() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            Assertions.assertThrows(DataAccessException.class, () -> {
                gameDAO.createGame(null);
            }, "Game created with empty name!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Get Game Successful")
    public void getGameSuccessful() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd = gameDAO.createGame("li nimla pleetik");

            GameData retrievedGD = gameDAO.getGame(gd.gameID());

            Assertions.assertEquals(gd.gameName(), retrievedGD.gameName(),
                    "Created and retrieved gameNames don't match!");
            Assertions.assertEquals(gd.blackUsername(), retrievedGD.blackUsername(),
                    "Created and retrieved blackUsernames don't match!");
            Assertions.assertEquals(gd.whiteUsername(), retrievedGD.whiteUsername(),
                    "Created and retrieved whiteUsernames don't match!");
            Assertions.assertEquals(gd.game(), retrievedGD.game(),
                    "Created and retrieved chess game objects don't match!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Get Nonexistent Game")
    public void getNonexistentGame() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd = gameDAO.createGame("li chaab'il pleetik");

            gameDAO.clear();

            GameData retrievedGD = gameDAO.getGame(gd.gameID());

            Assertions.assertNull(retrievedGD,
                    "GameData was returned when there wasn't one to begin with!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("List Games Successful")
    public void listGamesSuccessful() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd1 = gameDAO.createGame("li nimla rahilal");

            ListGameData lgd1 = new ListGameData(gd1.gameID(), gd1.whiteUsername(), gd1.blackUsername(), gd1.gameName());

            GameData gd2 = gameDAO.createGame("li pleet li mas yib' iru");

            ListGameData lgd2 = new ListGameData(gd2.gameID(), gd2.whiteUsername(), gd2.blackUsername(), gd2.gameName());

            Collection<ListGameData> gameList = new ArrayList<>();

            gameList.add(lgd1);
            gameList.add(lgd2);

            Assertions.assertEquals(gameList, gameDAO.listGames(),
                    "Games not listed correctly!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("List Games Empty")
    public void listGamesEmpty() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            gameDAO.clear();

            Assertions.assertEquals(new ArrayList<>(), gameDAO.listGames(),
                    "List games returned games when there were none!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Update Game Successful")
    public void updateGameSuccessful() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd = gameDAO.createGame("li saqi pleetik");

            GameData update = new GameData(gd.gameID(), "laj paap", "laj b'atz", gd.gameName(), gd.game());

            gameDAO.updateGame(update);

            GameData retrieved = gameDAO.getGame(gd.gameID());

            Assertions.assertEquals(update, retrieved,
                    "Game not updated properly!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }

    @Test
    @DisplayName("Update Game Empty")
    public void updateGameEmpty() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd = gameDAO.createGame("li saqi pleetik");

            GameData update = new GameData(null, null, null, null, null);

            gameDAO.updateGame(update);

            GameData retrieved = gameDAO.getGame(gd.gameID());

            Assertions.assertNull(retrieved.whiteUsername(),
                    "whiteUsername not deleted!");
            Assertions.assertNull(retrieved.blackUsername(),
                    "blackUsername not deleted!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


    @Test
    @DisplayName("Clear Successful")
    public void clearSuccessful() {

        try {

            GameDAO gameDAO = new SQLGameDAO();

            GameData gd1 = gameDAO.createGame("li kaq'i pleet");

            GameData gd2 = gameDAO.createGame("li pleet re estrategia");

            gameDAO.clear();

            GameData retrievedGD1 = gameDAO.getGame(gd1.gameID());

            GameData retrievedGD2 = gameDAO.getGame(gd2.gameID());

            Assertions.assertNull(retrievedGD1, "First game not cleared!");
            Assertions.assertNull(retrievedGD2, "Second game not cleared!");

        } catch (DataAccessException e) {
            Assertions.fail("Unexpected DataAccessException: " + e.getMessage());
        }

    }


}
