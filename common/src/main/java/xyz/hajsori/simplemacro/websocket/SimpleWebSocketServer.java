package xyz.hajsori.simplemacro.websocket;

import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import de.maxhenkel.voicechat.voice.client.ClientVoicechat;
import net.minecraft.util.Util;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import xyz.hajsori.simplemacro.Constants;

import java.net.InetSocketAddress;

public class SimpleWebSocketServer extends WebSocketServer {

    public SimpleWebSocketServer() {
        super(new InetSocketAddress(0));
    }


    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        if (Constants.CLIENT_CONFIG.logMessages.get()) {
            Constants.LOGGER.info("Connected to WebSocket Client with Port {}", this.getPort());
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
        if (Constants.CLIENT_CONFIG.logMessages.get()) {
            Constants.LOGGER.info("Disconnected from WebSocket Client with Port {}", this.getPort());
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        if (Constants.CLIENT_CONFIG.logMessages.get()) {
            Constants.LOGGER.info("Received message from WebSocket Client with Port {}: {}", this.getPort(), message);
        }
        new ActionManager(message, webSocket);
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        Constants.LOGGER.error("Simple Macro WebSocket Server ran into an Problem: " + e);
    }

    @Override
    public void onStart() {
        Util.getPlatform().openUri("streamdeck://plugins/message/xyz.hajsori.simplemacro.streamdeck/wss?streamdeck=hidden&port=" + this.getPort());
    }
}
