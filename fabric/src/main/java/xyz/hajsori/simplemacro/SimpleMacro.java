package xyz.hajsori.simplemacro;

import net.fabricmc.api.ModInitializer;

public class SimpleMacro implements ModInitializer {
    @Override
    public void onInitialize() {
        Constants.LOGGER.info("Simple Macro Fabric Initialized");
        CommonClass.init();
    }
}
