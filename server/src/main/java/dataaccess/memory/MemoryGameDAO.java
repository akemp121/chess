package dataaccess.memory;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;
import model.ListGameData;

import java.util.ArrayList;
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
        Integer id = createID();
        ChessGame game = new ChessGame();
        GameData gd = new GameData(id, null, null, gameName, game);
        games.put(id, gd);
        return gd;
    }

    @Override
    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public ArrayList<ListGameData> listGames() {
        ArrayList<ListGameData> gdConverted = new ArrayList<>();
        for (GameData gd : games.values()) {
            gdConverted.add(new ListGameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName()));
        }
        return gdConverted;
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
