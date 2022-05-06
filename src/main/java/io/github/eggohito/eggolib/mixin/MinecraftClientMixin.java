package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "setScreen", at = @At("HEAD"))
    private void eggolib$getCurrentScreen(Screen screen, CallbackInfo ci) {

        if (this.player == null) return;
        Eggolib.playerCurrentScreenHashMap.put(this.player, screen == null ? null : screen.getClass().getName());

    }

}
