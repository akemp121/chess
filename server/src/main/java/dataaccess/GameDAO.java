package dataaccess;

import model.*;

import java.util.ArrayList;

public interface GameDAO {
    Integer createID();
    GameData createGame(String gameName);
    GameData getGame(int gameID);
    ArrayList<ListGameData> listGames();
    void updateGame(GameData data);
    void clear();

}
