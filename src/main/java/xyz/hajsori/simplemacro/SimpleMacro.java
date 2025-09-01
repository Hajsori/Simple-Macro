package xyz.hajsori.simplemacro;

import de.maxhenkel.voicechat.configbuilder.ConfigBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.hajsori.simplemacro.websocket.WebSocketServer;

import java.nio.file.Path;

public class SimpleMacro implements ModInitializer {
    public static final String MOD_ID = "simplemacro";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static ClientConfig CLIENT_CONFIG;

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            LOGGER.error("Mod is loaded on a Server: This is a Client-Side Mod! Disabling Mod");
            return;
        }

        CLIENT_CONFIG = ConfigBuilder.builder(ClientConfig::new).path(Path.of(".").resolve("config").resolve(MOD_ID).resolve("simplemacro-client.properties")).build();

        WebSocketServer wss = new WebSocketServer();
        wss.start();
    }
}
