package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.EggolibClient;
import io.github.eggohito.eggolib.power.ModifyMouseSensitivityPower;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
@Environment(EnvType.CLIENT)
public abstract class MouseMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@ModifyVariable(method = "updateMouse", at = @At("STORE"), ordinal = 2)
	private double eggolib$modifyMouseSensitivity(double originalValue) {

		if (this.client.player == null) {
			return originalValue;
		}

		double newValue = PowerHolderComponent.modify(this.client.player, ModifyMouseSensitivityPower.class, originalValue);
		return newValue != originalValue ? newValue : originalValue;

	}

	@Inject(method = "onMouseButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V"), cancellable = true)
	private void eggolib$onMouseButtonPress(long window, int button, int action, int mods, CallbackInfo ci) {

		if (client.player == null) {
			return;
		}

		InputUtil.Key key = InputUtil.Type.MOUSE.createFromCode(button);
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
