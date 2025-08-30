package xyz.hajsori.simplestreamdeck.websocket;

import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import xyz.hajsori.simplestreamdeck.ActionManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {
    private static Logger LOGGER = null;

    public WebSocketServer(Logger LOGGER) {
        super(new InetSocketAddress(0));
        WebSocketServer.LOGGER = LOGGER;
    }


    @Override
    public void onOpen(org.java_websocket.WebSocket webSocket, ClientHandshake clientHandshake) {
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
    public void onClose(org.java_websocket.WebSocket webSocket, int i, String s, boolean b) {
        LOGGER.info("Disconnected from WebSocket Client with Port {}", this.getPort());
    }

    @Override
    public void onMessage(org.java_websocket.WebSocket webSocket, String s) {
        System.out.println("Received message: " + s);
        new ActionManager(s, webSocket);
    }

    @Override
    public void onError(org.java_websocket.WebSocket webSocket, Exception e) {

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
