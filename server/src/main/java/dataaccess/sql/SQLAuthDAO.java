package dataaccess.sql;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    @Override
    public AuthData createAuth(String userName) {
        return null;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() {

    }
}
