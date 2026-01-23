package xyz.hajsori.simplemacro;

import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class SimpleMacro {
    public SimpleMacro() {
        Constants.LOGGER.info("Simple Macro Forge Initialized");
        CommonClass.init();
    }
}
