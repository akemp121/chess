package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String userName) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken);
    void clear();
}
