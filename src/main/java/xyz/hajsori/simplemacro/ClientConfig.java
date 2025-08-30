package xyz.hajsori.simplemacro;

import de.maxhenkel.voicechat.configbuilder.ConfigBuilder;
import de.maxhenkel.voicechat.configbuilder.entry.ConfigEntry;

public class ClientConfig {
    public ConfigEntry<Integer> port;
    public ConfigEntry<Boolean> logMessages;

    public ClientConfig(ConfigBuilder builder) {
        builder.header(String.format("%s client config %s", "Simple Makro", "v1.21.8-0.0.1"));

        port = builder
                .integerEntry("port", 0, 0, 65535,
                        "The port to open the web socket server on",
                        "0 = random port"
                );
        logMessages = builder
                .booleanEntry("log_messages", false,
                        "Log sent and received messages"
                );
    }
}
