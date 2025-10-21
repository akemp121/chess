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
    public ArrayList<ListGameData> listGames() {
        ArrayList<ListGameData> gd_converted = new ArrayList<>();
        for (GameData gd : games.values()) {
            gd_converted.add(new ListGameData(gd.gameID(), gd.whiteUsername(), gd.blackUsername(), gd.gameName()));
        }
        return gd_converted;
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
