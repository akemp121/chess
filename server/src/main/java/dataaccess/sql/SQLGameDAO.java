package dataaccess.sql;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import model.ListGameData;
import dataaccess.DatabaseManager;
import java.sql.*;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO {

    @Override
    public Integer createID() {
        return 0;
    }

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public ArrayList<ListGameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData data) {

    }

    @Override
    public void clear() {

    }
}
