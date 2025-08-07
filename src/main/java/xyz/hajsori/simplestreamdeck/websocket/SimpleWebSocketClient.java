package xyz.hajsori.simplestreamdeck.websocket;

import com.google.gson.JsonObject;
import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import org.java_websocket.handshake.ServerHandshake;

import org.java_websocket.client.WebSocketClient;
import xyz.hajsori.simplestreamdeck.ActionManager;

import java.net.URI;

public class SimpleWebSocketClient extends WebSocketClient {
    public SimpleWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Verbindung zum Stream Deck hergestellt");

        ClientPlayerStateManager manager = ClientManager.getPlayerStateManager();

        JsonObject object = new JsonObject();
        object.addProperty("isDisabled", manager.isDisabled());
        object.addProperty("isHidden", VoicechatClient.CLIENT_CONFIG.hideIcons.get());
        object.addProperty("isMuted", manager.isMuted());
        object.addProperty("isRecording", false);

        this.send(object.toString());
    }

    @Override
    public void onMessage(String message) {
        new ActionManager(message, this);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Verbindung geschlossen");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void connectToStreamDeck() {
        try {
            URI serverUri = new URI("ws://localhost:8080");
            this.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
