package xyz.hajsori.simplestreamdeck;

import de.maxhenkel.voicechat.VoicechatClient;
import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.ClientPlayerStateManager;
import xyz.hajsori.simplestreamdeck.websocket.SimpleWebSocketClient;

public class ActionManager {
    ClientPlayerStateManager playerStates = ClientManager.getPlayerStateManager();

    public ActionManager(String action, SimpleWebSocketClient ws) {
        switch (action) {
            case "toggleMicrophone":
                boolean muted = !playerStates.isMuted();
                playerStates.setMuted(muted);
                ws.send("{isMuted:" + muted + "}");
                break;
            case "toggleVoicechat":
                boolean disabled = !playerStates.isDisabled();
                playerStates.setDisabled(disabled);
                ws.send("{isDisabled:" + disabled + "}");
                break;
            case "toggleIcons":
                boolean hidden = !VoicechatClient.CLIENT_CONFIG.hideIcons.get();
                VoicechatClient.CLIENT_CONFIG.hideIcons.set(hidden);
                ws.send("{isHidden:" + hidden + "}");
                break;
            case "toggleRecording":
                ClientManager.getClient().toggleRecording();
                break;
        }
    }
}
