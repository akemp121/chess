package dataaccess.sql;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import dataaccess.DatabaseManager;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

import static java.sql.Types.NULL;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(password),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    @Override
    public void createUser(UserData data) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        UserData toWrite = new UserData(data.username(), hashedPassword, data.email());
        String json = new Gson().toJson(toWrite);
        executeUpdate(statement, toWrite.username(), hashedPassword, toWrite.email(), json);
    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, json FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, userName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
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
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
