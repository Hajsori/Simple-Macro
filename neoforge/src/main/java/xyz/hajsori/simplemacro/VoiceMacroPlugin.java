package xyz.hajsori.simplemacro;

import de.maxhenkel.voicechat.api.ForgeVoicechatPlugin;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientVoicechatConnectionEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import xyz.hajsori.simplemacro.websocket.SimpleWebSocketServer;

@ForgeVoicechatPlugin
public class VoiceMacroPlugin implements VoicechatPlugin {
    private static SimpleWebSocketServer wss;

    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(ClientVoicechatConnectionEvent.class, this::onClientVoicechatConnection);
    }

    @Override
    public String getPluginId() {
        return Constants.MOD_ID;
    }

    public void onClientVoicechatConnection(ClientVoicechatConnectionEvent event) {
        if (event.isConnected() && wss == null) {
            wss = new SimpleWebSocketServer();
            wss.start();
        } else if (!event.isConnected() && wss != null) {
            try {
                wss.stop();
            } catch (InterruptedException e) {
                Constants.LOGGER.warn("Interrupted while stopping WebSocket server", e);
                Thread.currentThread().interrupt();
            }

            wss = null;
        }
    }
}
