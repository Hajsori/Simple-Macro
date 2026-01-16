package xyz.hajsori.simplemacro.websocket;

import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import net.minecraft.Util;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.slf4j.Logger;
import xyz.hajsori.simplemacro.Constants;
import xyz.hajsori.simplemacro.config.ClientConfig;

import java.net.InetSocketAddress;

public class SimpleWebSocketServer extends WebSocketServer {
    private static Logger LOGGER;
    private static ClientConfig CLIENT_CONFIG;

    public SimpleWebSocketServer() {
        super(new InetSocketAddress(0));
        SimpleWebSocketServer.LOGGER = Constants.LOGGER;
        SimpleWebSocketServer.CLIENT_CONFIG = Constants.CLIENT_CONFIG;
    }


    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        if (CLIENT_CONFIG.logMessages.get()) {
            LOGGER.info("Connected to WebSocket Client with Port {}", this.getPort());
        }

        ClientPlayerStateManager manager = ClientManager.getPlayerStateManager();
        ClientVoicechat client = ClientManager.getClient();

        JsonObject object = new JsonObject();
        object.addProperty("isDisabled", manager.isDisabled());
        object.addProperty("isHidden", VoicechatClient.CLIENT_CONFIG.hideIcons.get());
        object.addProperty("isMuted", manager.isMuted());
        object.addProperty("isRecording", client != null && client.getRecorder() != null);

        webSocket.send(object.toString());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        if (CLIENT_CONFIG.logMessages.get()) {
            LOGGER.info("Disconnected from WebSocket Client with Port {}", this.getPort());
        }
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
        if (CLIENT_CONFIG.logMessages.get()) {
            LOGGER.info("Started WebSocket Server on port {}", this.getPort());
        }

        Util.getPlatform().openUri("streamdeck://plugins/message/xyz.hajsori.simplemacro.streamdeck/wss?streamdeck=hidden&port=" + this.getPort());
    }
}
