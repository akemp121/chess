package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData createGame(String gameName);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame(GameData data);
    void clear();

}
