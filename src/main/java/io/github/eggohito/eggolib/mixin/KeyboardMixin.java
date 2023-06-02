package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.EggolibClient;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncPreventedKeyPacket;
import io.github.eggohito.eggolib.power.PreventKeyUsePower;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

	@Shadow
	@Final
	private MinecraftClient client;

	@Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
	private void eggolib$preventKeyUse(long window, int keycode, int scancode, int action, int modifiers, CallbackInfo ci) {

		if (client.player == null || client.currentScreen != null) {
			return;
		}

		InputUtil.Key key = InputUtil.fromKeyCode(keycode, scancode);
		KeyBinding keyBinding = KeyBindingAccessor.getKeyAndBindingMap().get(key);

		if (keyBinding == null || !eggolib$preventsKey(keyBinding, keycode, scancode)) {
			return;
		}

		EggolibClient.PREVIOUS_KEY_BINDING_STATES.put(keyBinding.getTranslationKey(), true);
		KeyBinding.setKeyPressed(key, false);

		ci.cancel();

	}

	@Inject(method = "onKey", at = @At("TAIL"))
	private void eggolib$syncKeyPress(long window, int keycode, int scancode, int action, int modifiers, CallbackInfo ci) {

		if (client.player == null || client.currentScreen != null) {
			return;
		}

		InputUtil.Key key = InputUtil.fromKeyCode(keycode, scancode);
		KeyBinding keyBinding = KeyBindingAccessor.getKeyAndBindingMap().get(key);

		if (keyBinding != null) {
			EggolibClient.PREVIOUS_KEY_BINDING_STATES.put(keyBinding.getTranslationKey(), keyBinding.isPressed());
		}

	}

	@Unique
	private boolean eggolib$preventsKey(KeyBinding keyBinding, int keycode, int scancode) {

		List<Identifier> powerIds = PowerHolderComponent.getPowers(client.player, PreventKeyUsePower.class)
			.stream()
			.filter(pkup -> pkup.doesApply(keycode, scancode))
			.map(pkup -> pkup.getType().getIdentifier())
			.toList();
		if (powerIds.isEmpty()) {
			return false;
		}

		ClientPlayNetworking.send(new SyncPreventedKeyPacket(keyBinding.getTranslationKey(), powerIds));
		return true;

	}

}
