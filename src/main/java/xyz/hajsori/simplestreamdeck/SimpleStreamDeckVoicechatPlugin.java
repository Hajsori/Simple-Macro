package xyz.hajsori.simplestreamdeck;

import de.maxhenkel.voicechat.api.VoicechatApi;
import de.maxhenkel.voicechat.api.VoicechatPlugin;
import de.maxhenkel.voicechat.api.events.ClientVoicechatInitializationEvent;
import de.maxhenkel.voicechat.api.events.EventRegistration;
import xyz.hajsori.simplestreamdeck.websocket.SimpleWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

public class SimpleStreamDeckVoicechatPlugin implements VoicechatPlugin {

    /**
     * @return the unique ID for this voice chat plugin
     */
    @Override
    public String getPluginId() {
        return SimpleStreamDeck.MOD_ID;
    }

    /**
     * Called when the voice chat initializes the plugin.
     *
     * @param api the voice chat API
     */
    @Override
    public void initialize(VoicechatApi api) {
        SimpleStreamDeck.LOGGER.info("Example voice chat plugin initialized!");
    }

    /**
     * Called once by the voice chat to register all events.
     *
     * @param registration the event registration
     */
    @Override
    public void registerEvents(EventRegistration registration) {
        registration.registerEvent(ClientVoicechatInitializationEvent.class, this::onClientVoicechatInitialization);
    }


    public void onClientVoicechatInitialization(ClientVoicechatInitializationEvent event) {
        try {
            new SimpleWebSocketClient(new URI("ws://localhost:8080")).connectToStreamDeck();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}