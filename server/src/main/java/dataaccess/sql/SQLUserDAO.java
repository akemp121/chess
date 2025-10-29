package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import dataaccess.DatabaseManager;
import java.sql.*;

public class SQLUserDAO implements UserDAO {

    @Override
    public void createUser(UserData data) {

    }

    @Override
    public UserData getUser(String userName) {
        return null;
    }

    @Override
    public void clear() {

    }
}
