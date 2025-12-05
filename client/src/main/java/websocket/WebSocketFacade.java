package websocket;

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

public class WebSocketFacade extends Endpoint {

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
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

}
