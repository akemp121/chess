package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(String userName);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
}
