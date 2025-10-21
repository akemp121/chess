package dataaccess.memory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {

    private final Map<Integer, GameData> games = new HashMap<>();
    private Integer sequential = 1000;

    @Override
    public Integer createID() {
        sequential += 1;
        return sequential;
    }

    @Override
    public GameData createGame(String gameName) {
        Integer ID = createID();
        ChessGame game = new ChessGame();
        GameData gd = new GameData(ID, "", "", gameName, game);
        games.put(ID, gd);
        return gd;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
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
