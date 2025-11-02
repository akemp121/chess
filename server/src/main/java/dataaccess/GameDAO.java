package dataaccess;

import model.*;

import java.util.ArrayList;

public interface GameDAO {
    GameData createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<ListGameData> listGames() throws DataAccessException;
    void updateGame(GameData data) throws DataAccessException;
    void clear() throws DataAccessException;

}
