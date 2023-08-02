package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.EggolibClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

	@Inject(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferRenderer;reset()V"))
	private void eggolib$syncScreen(Screen screen, CallbackInfo ci) {
		EggolibClient.SCREEN_TICKER_UTIL.start(screen);
	}

}
