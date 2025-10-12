package xyz.hajsori.simplemacro.mixin;

import de.maxhenkel.voicechat.voice.client.PTTKeyHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import xyz.hajsori.simplemacro.Constants;
import xyz.hajsori.simplemacro.config.ConsumeKeys;

@Mixin(PTTKeyHandler.class)
public class PTTKeyHandlerMixin {
    @Shadow(remap = false) private boolean pttKeyDown;
    @Shadow(remap = false) private boolean whisperKeyDown;

    @Overwrite(remap = false)
    public boolean isPTTDown() {
        Constants.LOGGER.info("PTTKeyHandler: isPTTDown");
        return pttKeyDown || ConsumeKeys.pttKeyDown;
    }

    @Overwrite(remap = false)
    public boolean isWhisperDown() {
        return whisperKeyDown || ConsumeKeys.whisperKeyDown;
    }

    @Overwrite(remap = false)
    public boolean isAnyDown() {
        return pttKeyDown || whisperKeyDown || ConsumeKeys.pttKeyDown || ConsumeKeys.whisperKeyDown;
    }
}
