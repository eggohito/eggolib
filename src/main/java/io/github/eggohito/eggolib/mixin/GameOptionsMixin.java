package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Shadow protected MinecraftClient client;

    @Inject(method = "setPerspective", at = @At("HEAD"))
    private void eggolib$getCurrentPerspective(Perspective perspective, CallbackInfo ci) {

        if (this.client.player == null) return;
        Eggolib.playerCurrentPerspectiveHashMap.put(this.client.player, perspective == null ? null : perspective.name());

    }

}
