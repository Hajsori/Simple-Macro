package xyz.hajsori.simplemacro;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SimpleMacro {

    public SimpleMacro(IEventBus eventBus) {
        Constants.LOGGER.info("Simple Macro NeoForge Initialized");
        CommonClass.init();
    }
}
