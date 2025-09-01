package xyz.hajsori.simplemacro.websocket;

import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import xyz.hajsori.simplemacro.ActionManager;
import xyz.hajsori.simplemacro.ClientConfig;
import xyz.hajsori.simplemacro.SimpleMacro;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {
    private static Logger LOGGER;
    private static ClientConfig CLIENT_CONFIG;

    public WebSocketServer() {
        super(new InetSocketAddress(0));
        WebSocketServer.LOGGER = SimpleMacro.LOGGER;
        WebSocketServer.CLIENT_CONFIG = SimpleMacro.CLIENT_CONFIG;
    }


    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        LOGGER.info("Connected to WebSocket Client with Port {}", this.getPort());

        ClientPlayerStateManager manager = ClientManager.getPlayerStateManager();

        JsonObject object = new JsonObject();
        object.addProperty("isDisabled", manager.isDisabled());
        object.addProperty("isHidden", VoicechatClient.CLIENT_CONFIG.hideIcons.get());
        object.addProperty("isMuted", manager.isMuted());
        object.addProperty("isRecording", false);

        webSocket.send(object.toString());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        LOGGER.info("Disconnected from WebSocket Client with Port {}", this.getPort());
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        if (CLIENT_CONFIG.logMessages.get()) {
            LOGGER.info("Received message from WebSocket Client with Port {}: {}", this.getPort(), message);
        }
        new ActionManager(message, webSocket);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        String url = "streamdeck://plugins/message/xyz.hajsori.simplestreamdeckplugin/wss?streamdeck=hidden&port=" + this.getPort();
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + url + "\"");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
