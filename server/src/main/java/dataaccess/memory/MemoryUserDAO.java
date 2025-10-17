package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;
import java.util.Map;


public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void createUser(UserData data) {
        users.put(data.username(), data);
    }

    @Override
    public UserData getUser(String userName) {
        return users.get(userName);
    }

    @Override
    public void clear() {
        users.clear();
    }

}
