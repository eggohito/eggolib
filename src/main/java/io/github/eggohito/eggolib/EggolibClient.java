package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.compat.EggolibModCompatClient;
import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import io.github.eggohito.eggolib.integration.EggolibPowerIntegration;
import io.github.eggohito.eggolib.networking.EggolibPacketsS2C;
import io.github.eggohito.eggolib.networking.packet.c2s.EndKeySequencePacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncKeyPressPacket;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.util.Key;
import io.github.eggohito.eggolib.util.ticker.PerspectiveTickerUtil;
import io.github.eggohito.eggolib.util.ticker.ScreenTickerUtil;
import io.github.eggohito.eggolib.util.key.FunctionalKey;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class EggolibClient implements ClientModInitializer {

	public static final Map<KeyBinding, Boolean> PREVENTED_KEY_BINDINGS = new HashMap<>();

	public static final Map<String, Boolean> PREVIOUS_KEY_BINDING_STATES = new HashMap<>();
	public static final Map<String, KeyBinding> ID_TO_KEYBINDING_MAP = new HashMap<>();

	public static final PerspectiveTickerUtil PERSPECTIVE_TICKER_UTIL = new PerspectiveTickerUtil();
	public static final ScreenTickerUtil SCREEN_TICKER_UTIL = new ScreenTickerUtil();

	private static boolean initializedKeyBindingMap = false;

	@Override
	public void onInitializeClient() {

		ClientPlayConnectionEvents.INIT.register((handler, client) -> Eggolib.registryManager = () -> Optional.of(handler.getRegistryManager()));

		//  Register the packets and the class data registries
		EggolibPacketsS2C.register();
		EggolibClassDataClient.register();

		//  Register client callbacks used by some power types
		EggolibPowerIntegration.registerClient();

		//  Initialize client compat. stuff
		FabricLoader.getInstance()
			.getEntrypointContainers("eggolib:compat/client", EggolibModCompatClient.class)
			.stream()
			.map(EntrypointContainer::getEntrypoint)
			.forEach(EggolibModCompatClient::init);

		//  Tick ticker utilities
		ClientTickEvents.START_CLIENT_TICK.register(
			minecraftClient -> {
				PERSPECTIVE_TICKER_UTIL.tick();
				SCREEN_TICKER_UTIL.tick();
			}
		);

	}

	public static void addPowerKeyBinding(String keyName, KeyBinding keyBinding) {
		ID_TO_KEYBINDING_MAP.put(keyName, keyBinding);
	}

	public static void syncKeyPresses(Map<ActionOnKeySequencePower, FunctionalKey> powerAndKeyMap) {

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

	public static void compareKeySequences(Map<ActionOnKeySequencePower, FunctionalKey> powerAndKeyMap) {

		Map<Identifier, Boolean> powerIdAndMatchingSequenceMap = new HashMap<>();
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

	public static KeyBinding getKeyBinding(String name) {
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
