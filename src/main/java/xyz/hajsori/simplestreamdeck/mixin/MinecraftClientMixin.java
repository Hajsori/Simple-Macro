package xyz.hajsori.simplestreamdeck.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Unique
    private boolean hasInitialized = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient)(Object)this;
        if (!hasInitialized && client.world != null) {
            hasInitialized = true;
        }
    }
}
