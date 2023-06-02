package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.EggolibClient;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncPreventedKeyPacket;
import io.github.eggohito.eggolib.power.ModifyMouseSensitivityPower;
import io.github.eggohito.eggolib.power.PreventKeyUsePower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

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

		if (newValue != originalValue) {
			return newValue;
		} else {
			return originalValue;
		}

	}

	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
	private void eggolib$preventMouseUse(long window, int button, int action, int mods, CallbackInfo ci) {

		if (client.player == null || client.currentScreen != null) {
			return;
		}

		InputUtil.Key key = InputUtil.Type.MOUSE.createFromCode(button);
		KeyBinding keyBinding = KeyBindingAccessor.getKeyAndBindingMap().get(key);

		if (keyBinding == null || !eggolib$preventsMouse(keyBinding, button)) {
			return;
		}

		EggolibClient.PREVIOUS_KEY_BINDING_STATES.put(keyBinding.getTranslationKey(), true);
		KeyBinding.setKeyPressed(key, false);

		ci.cancel();

	}

	@Inject(method = "onMouseButton", at = @At("TAIL"))
	private void eggolib$syncMousePress(long window, int button, int action, int mods, CallbackInfo ci) {

		if (client.player == null || client.currentScreen != null) {
			return;
		}

		InputUtil.Key key = InputUtil.Type.MOUSE.createFromCode(button);
		KeyBinding keyBinding = KeyBindingAccessor.getKeyAndBindingMap().get(key);

		if (keyBinding != null) {
			EggolibClient.PREVIOUS_KEY_BINDING_STATES.put(keyBinding.getTranslationKey(), keyBinding.isPressed());
		}

	}

	@Unique
	private boolean eggolib$preventsMouse(KeyBinding keyBinding, int mousecode) {

		List<Identifier> powerIds = PowerHolderComponent.getPowers(client.player, PreventKeyUsePower.class)
			.stream()
			.filter(pkup -> pkup.doesApply(mousecode))
			.map(pkup -> pkup.getType().getIdentifier())
			.toList();
		if (powerIds.isEmpty()) {
			return false;
		}

		ClientPlayNetworking.send(new SyncPreventedKeyPacket(keyBinding.getTranslationKey(), powerIds));
		return true;

	}

}
