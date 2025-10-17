package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData existingRecord = games.get(gameID);
        if (existingRecord == null) {
            throw new DataAccessException("Game not found!");
        }
        return existingRecord;
    }

    @Override
    public Collection<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData data) {
        games.put(data.gameID(), data);
    }

    @Override
    public void clear() {
        games.clear();
    }
}
