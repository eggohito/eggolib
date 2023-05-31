package io.github.eggohito.eggolib.mixin.apace100.apoli;

import io.github.apace100.apoli.ApoliClient;
import io.github.eggohito.eggolib.EggolibClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ApoliClient.class)
public abstract class ApoliClientMixin {

	@Inject(method = "registerPowerKeybinding", at = @At("HEAD"))
	private static void eggolib$addPowerKeybinding(String keyId, KeyBinding keyBinding, CallbackInfo ci) {
		EggolibClient.addPowerKeyBinding(keyId, keyBinding);
	}

}
