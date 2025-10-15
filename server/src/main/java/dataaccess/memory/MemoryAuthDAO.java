package dataaccess.memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import java.util.UUID;

import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    Map<String, AuthData> authData;

    @Override
    public AuthData createAuth(String userName) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData data = new AuthData(authToken, userName);
        authData.put(authToken, data);
        return data;
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData existingRecord = authData.get(authToken);
        if (existingRecord == null) {
            throw new DataAccessException("Auth token not found!");
        }
        return existingRecord;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if(authData.remove(authToken) == null) {
            throw new DataAccessException("Auth token doesn't exist!");
        }
    }

    @Override
    public void clear() {
        authData.clear();
    }
}
