package dataaccess.memory;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private final Map<String, AuthData> authData = new HashMap<>();

    @Override
    public AuthData createAuth(String userName) {
        String authToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(authToken, userName);
        authData.put(authToken, data);
        return data;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authData.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authData.remove(authToken);
    }

    @Override
    public void clear() {
        authData.clear();
    }
}
