package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.Map;


public class MemoryUserDAO implements UserDAO {

    Map<String, UserData> users;

    @Override
    public void createUser(UserData data) {
        users.put(data.username(), data);
    }

    @Override
    public UserData getUser(String userName) throws DataAccessException {
        UserData existingRecord = users.get(userName);
        if (existingRecord == null) {
            throw new DataAccessException("User not found!");
        }
        return existingRecord;
    }

    @Override
    public void clear() {
        users.clear();
    }

}
