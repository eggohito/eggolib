package io.github.eggohito.eggolib;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.eggohito.eggolib.compat.IEggolibModCompat;
import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import io.github.eggohito.eggolib.networking.EggolibPacketsS2C;
import io.github.eggohito.eggolib.networking.packet.c2s.EndKeySequencePacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncKeyPressPacket;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.util.Key;
import io.github.eggohito.eggolib.util.key.FunctionalKey;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class EggolibClient implements ClientModInitializer {

	private static final HashMap<String, KeyBinding> ID_TO_KEYBINDING_MAP = new HashMap<>();
	private static HashMap<String, Boolean> previousKeyBindingStates = new HashMap<>();
	private static boolean initializedKeyBindingMap = false;

	@Override
	public void onInitializeClient() {

		//  Register the packets and the class data registries
		EggolibPacketsS2C.register();
		EggolibClassDataClient.register();

		//  Initialize client compat. stuff
		FabricLoader.getInstance()
			.getEntrypointContainers("eggolib:compat/client", IEggolibModCompat.class)
			.stream()
			.map(EntrypointContainer::getEntrypoint)
			.forEach(
				iEggolibModCompat -> {

					String modCompatTarget = iEggolibModCompat.getCompatTarget();

					if (modCompatTarget == null) {
						iEggolibModCompat.init();
					} else {
						FabricLoader.getInstance().getModContainer(modCompatTarget).ifPresent(iEggolibModCompat::initOn);
					}

				}
			);

		//  Track which keybinds the player is pressing
		ClientTickEvents.START_CLIENT_TICK.register(
			minecraftClient -> {

				if (minecraftClient.player == null) {
					return;
				}

				List<ActionOnKeySequencePower> powers = PowerHolderComponent.getPowers(minecraftClient.player, ActionOnKeySequencePower.class).stream().filter(Power::isActive).toList();
				if (powers.isEmpty()) {
					return;
				}

				HashMap<ActionOnKeySequencePower, FunctionalKey> triggeredPowers = new HashMap<>();
				HashMap<String, Boolean> currentKeyBindingStates = new HashMap<>();

				for (ActionOnKeySequencePower power : powers) {

					List<FunctionalKey> keys = power.getKeys();
					for (FunctionalKey key : keys) {

						KeyBinding keyBinding = getKeyBinding(key.key);
						if (keyBinding == null) {
							continue;
						}

						currentKeyBindingStates.put(key.key, keyBinding.isPressed());
						if (currentKeyBindingStates.get(key.key) && (key.continuous || !previousKeyBindingStates.getOrDefault(key.key, false))) {
							triggeredPowers.put(power, key);
						}

					}

				}

				previousKeyBindingStates = currentKeyBindingStates;
				if (!triggeredPowers.isEmpty()) {
					syncKeyPresses(triggeredPowers);
					compareKeySequences(triggeredPowers);
				}

			}
		);

	}

	public static void addPowerKeyBinding(String keyName, KeyBinding keyBinding) {
		ID_TO_KEYBINDING_MAP.put(keyName, keyBinding);
	}

	private void syncKeyPresses(Map<ActionOnKeySequencePower, FunctionalKey> powerAndKeyMap) {

		Map<Identifier, String> powerIdAndKeyStringMap = new HashMap<>();
		for (Map.Entry<ActionOnKeySequencePower, FunctionalKey> powerAndKey : powerAndKeyMap.entrySet()) {

			ActionOnKeySequencePower power = powerAndKey.getKey();
			Key key = new Key(powerAndKey.getValue().key);

			power.addKeyToSequence(key);
			powerIdAndKeyStringMap.put(power.getType().getIdentifier(), key.key);

		}

		if (!powerIdAndKeyStringMap.isEmpty()) {
			ClientPlayNetworking.send(new SyncKeyPressPacket(powerIdAndKeyStringMap));
		}

	}

	private void compareKeySequences(HashMap<ActionOnKeySequencePower, FunctionalKey> powerAndKeyMap) {

		HashMap<Identifier, Boolean> powerIdAndMatchingSequenceMap = new HashMap<>();
		for (ActionOnKeySequencePower power : powerAndKeyMap.keySet()) {

			List<String> specifiedKeySequence = power.getSpecifiedKeySequence().stream().map(key -> key.key).toList();
			List<String> currentKeySequence = power.getCurrentKeySequence().stream().map(key -> key.key).toList();

			if (specifiedKeySequence.equals(currentKeySequence)) {
				power.onSuccess();
				powerIdAndMatchingSequenceMap.put(power.getType().getIdentifier(), true);
			} else if (currentKeySequence.size() >= specifiedKeySequence.size()) {
				power.onFail();
				powerIdAndMatchingSequenceMap.put(power.getType().getIdentifier(), false);
			}

		}

		if (!powerIdAndMatchingSequenceMap.isEmpty()) {
			ClientPlayNetworking.send(new EndKeySequencePacket(powerIdAndMatchingSequenceMap));
		}

	}

	private KeyBinding getKeyBinding(String name) {
		if (ID_TO_KEYBINDING_MAP.containsKey(name)) {
			return ID_TO_KEYBINDING_MAP.get(name);
		} else if (initializedKeyBindingMap) {
			return null;
		} else {

			initializedKeyBindingMap = true;

			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			KeyBinding[] keyBindings = minecraftClient.options.allKeys;

			for (KeyBinding keyBinding : keyBindings) {
				ID_TO_KEYBINDING_MAP.put(keyBinding.getTranslationKey(), keyBinding);
			}
			return getKeyBinding(name);

		}
	}

}
