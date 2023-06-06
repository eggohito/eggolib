package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.EggolibClient;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/InputUtil;fromKeyCode(II)Lnet/minecraft/client/util/InputUtil$Key;"), cancellable = true)
	private void eggolib$onKeyPress(long window, int keycode, int scancode, int action, int modifiers, CallbackInfo ci) {

		if (client.player == null) {
			return;
		}

		InputUtil.Key key = InputUtil.fromKeyCode(keycode, scancode);
		KeyBinding keyBinding = KeyBindingAccessor.getKeyAndBindingMap().get(key);

		if (keyBinding == null) {
			return;
		}

		if (action == 0) {
			EggolibClient.PREVENTED_KEY_BINDINGS.put(keyBinding, false);
		}

		else if (MiscUtilClient.shouldPreventKey(keyBinding, client)) {

			EggolibClient.PREVENTED_KEY_BINDINGS.put(keyBinding, true);
			KeyBinding.setKeyPressed(key, false);

			ci.cancel();

		}

	}

}
