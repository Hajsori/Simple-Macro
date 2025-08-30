package xyz.hajsori.simplestreamdeck;

import de.maxhenkel.voicechat.configbuilder.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hajsori.simplestreamdeck.websocket.WebSocketServer;

import java.awt.*;
import java.nio.file.Path;

public class SimpleStreamDeck implements ModInitializer {
    public static final String MOD_ID = "simplestreamdeck";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            LOGGER.error("Mod is loaded on a Server: This is a Client-Side Mod! Disabling Mod");
            return;
        }

        ConfigBuilder.builder(ClientConfig::new).path(Path.of(".").resolve("config").resolve(MOD_ID).resolve("simplemakro-client.properties")).build();

        WebSocketServer wss = new WebSocketServer(LOGGER);
        wss.start();
    }
}
