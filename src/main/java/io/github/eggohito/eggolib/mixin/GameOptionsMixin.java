package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.EggolibClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

	@Inject(method = "setPerspective", at = @At("HEAD"))
	private void eggolib$getCurrentPerspective(Perspective perspective, CallbackInfo ci) {
		EggolibClient.PERSPECTIVE_TICKER_UTIL.start(perspective);
	}

}
