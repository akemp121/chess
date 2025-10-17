package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String userName);
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear();
}
