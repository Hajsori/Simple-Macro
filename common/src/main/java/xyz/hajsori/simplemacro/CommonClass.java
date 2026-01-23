package xyz.hajsori.simplemacro;

import de.maxhenkel.voicechat.configbuilder.ConfigBuilder;
import xyz.hajsori.simplemacro.config.ClientConfig;
import xyz.hajsori.simplemacro.platform.Services;
import xyz.hajsori.simplemacro.websocket.SimpleWebSocketServer;

import java.nio.file.Path;

import static xyz.hajsori.simplemacro.Constants.MOD_ID;

public class CommonClass {
    public static void init() {
        Constants.LOGGER.info("Simple Macro Common Initialized");
        Constants.CLIENT_CONFIG = ConfigBuilder.builder(ClientConfig::new).path(Path.of(".").resolve("config").resolve(MOD_ID).resolve(MOD_ID + "-client.properties")).build();
        Constants.LOGGER.info("Simple Macro Log Messages is set to " + Constants.CLIENT_CONFIG.logMessages.get());

        SimpleWebSocketServer wss = new SimpleWebSocketServer();
        wss.start();
    }
}
