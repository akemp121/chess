package websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.sun.nio.sctp.Notification;
import jakarta.websocket.*;

import responses.ErrorResponse;
import websocket.commands.*;
import websocket.messages.*;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint implements MessageHandler {

    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler gameHandler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.gameHandler = gameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                        switch (serverMessage.getServerMessageType()) {
                            case NOTIFICATION:
                                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                                gameHandler.printMessage(notificationMessage.getMessage());
                                break;
                            case ERROR:
                                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                                gameHandler.printMessage(errorMessage.getErrorMessage());
                                break;
                            case LOAD_GAME:
                                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                                gameHandler.updateGame(loadGameMessage.getGame());
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void joinGame(String authToken, Integer gameID, ChessGame.TeamColor color) {
        try {
            UserConnectCommand command = new UserConnectCommand(UserGameCommand.CommandType.CONNECT,
                    authToken, gameID, UserConnectCommand.ConnectionType.PLAYER, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void observeGame(String authToken, Integer gameID) {
        try {
            UserConnectCommand command = new UserConnectCommand(UserGameCommand.CommandType.CONNECT,
                    authToken, gameID, UserConnectCommand.ConnectionType.OBSERVER, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void leaveGame(String authToken, Integer gameID) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE,
                    authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move) {
        try {
            MakeMoveCommand command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE,
                    authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN,
                    authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    public void redraw(String authToken, Integer gameID) {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.REDRAW,
                    authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

}
