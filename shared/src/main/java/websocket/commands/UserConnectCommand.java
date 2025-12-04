package websocket.commands;

import chess.ChessGame;

import java.util.Objects;

public class UserConnectCommand extends UserGameCommand {

    private final ConnectionType connectionType;
    private final ChessGame.TeamColor teamColor;

    public UserConnectCommand(CommandType commandType, String authToken, Integer gameID,
                              ConnectionType connectionType, ChessGame.TeamColor teamColor) {
        super(commandType, authToken, gameID);
        this.connectionType = connectionType;
        this.teamColor = teamColor;
    }

    public enum ConnectionType {
        PLAYER,
        OBSERVER
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        UserConnectCommand that = (UserConnectCommand) o;
        return connectionType == that.connectionType && teamColor == that.teamColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connectionType, teamColor);
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }
}
