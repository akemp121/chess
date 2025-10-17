package dataaccess;

import model.UserData;

public interface UserDAO {
    void createUser(UserData data);
    UserData getUser(String userName);
    void clear();
}
