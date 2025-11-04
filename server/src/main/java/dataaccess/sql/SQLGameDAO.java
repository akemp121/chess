package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import model.ListGameData;
import dataaccess.DatabaseManager;
import java.sql.*;

import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO {

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (gameName, game) VALUES (?, ?)";
        ChessGame toAdd = new ChessGame();
        String game = new Gson().toJson(toAdd);
        int id = executeUpdate(statement, gameName, game);
        GameData gd = new GameData(id, null, null, gameName, toAdd);
        String json = new Gson().toJson(gd);
        var statement2 = "UPDATE game SET json=? WHERE gameID=?";
        executeUpdate(statement2, json, id);
        return gd;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, game, json FROM game WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    @Override
    public ArrayList<ListGameData> listGames() throws DataAccessException {
        ArrayList<ListGameData> gdConverted = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT json FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        GameData gd = readGame(rs);
                        gdConverted.add(new ListGameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName()));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Error: Unable to read data: %s", e.getMessage()));
        }
        return gdConverted;
    }

    @Override
    public void updateGame(GameData data) throws DataAccessException {
        var statement = "UPDATE game SET whiteUsername=?, blackUsername=?, game=?, json=? WHERE gameID=?";
        String gameJson = new Gson().toJson(data.game());
        String json = new Gson().toJson(data);
        executeUpdate(statement, data.whiteUsername(),data.blackUsername(), gameJson, json, data.gameID());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", ex.getMessage()));
        }
    }
}
