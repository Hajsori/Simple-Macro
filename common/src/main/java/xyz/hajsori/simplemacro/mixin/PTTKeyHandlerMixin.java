package xyz.hajsori.simplemacro.mixin;

import de.maxhenkel.voicechat.voice.client.PTTKeyHandler;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.hajsori.simplemacro.config.ConsumeKeys;

@Debug(export = true)
@Mixin(PTTKeyHandler.class)
public class PTTKeyHandlerMixin {
    @Shadow() private boolean pttKeyDown;
    @Shadow() private boolean whisperKeyDown;

    @Inject(method = "isPTTDown", at = @At("RETURN"), cancellable = true, remap = false)
    public void isPTTDown(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(pttKeyDown || ConsumeKeys.pttKeyDown);
    }

    @Inject(method = "isWhisperDown", at = @At("RETURN"), cancellable = true, remap = false)
    public void isWhisperDown(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(whisperKeyDown || ConsumeKeys.whisperKeyDown);
    }

    @Inject(method = "isAnyDown", at = @At("RETURN"), cancellable = true, remap = false)
    public void isAnyDown(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(pttKeyDown || whisperKeyDown || ConsumeKeys.pttKeyDown || ConsumeKeys.whisperKeyDown);
    }
}
