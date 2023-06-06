package io.github.eggohito.eggolib.util;

import com.google.common.collect.HashBiMap;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.EggolibClient;
import io.github.eggohito.eggolib.mixin.apace100.calio.ClassDataRegistryAccessor;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncPerspectivePacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncPreventedKeyPacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncScreenStatePacket;
import io.github.eggohito.eggolib.power.PreventKeyUsePower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MiscUtilClient {

	public static void getScreenState(MinecraftClient client) {
		getScreenState(client, client.currentScreen);
	}

	@SuppressWarnings("rawtypes")
	public static void getScreenState(MinecraftClient client, Screen screen) {

		if (client.player == null) {
			return;
		}

		boolean inScreen = false;
		String screenClassName = null;
		Optional<ClassDataRegistry> opt$inGameScreenCDR = ClassDataRegistry.get(Screen.class);

		if (opt$inGameScreenCDR.isPresent() && screen != null) {

			ClassDataRegistry<?> inGameScreenCDR = opt$inGameScreenCDR.get();
			Class<?> screenClass = screen.getClass();
			inScreen = true;

			HashBiMap<String, Class<?>> inGameScreens = HashBiMap.create(((ClassDataRegistryAccessor) inGameScreenCDR).getMappings());
			if (inGameScreens.containsValue(screenClass)) {
				screenClassName = inGameScreens.inverse().get(screenClass);
			}

		}

		ScreenState screenState = new ScreenState(inScreen, screenClassName);
		if (Eggolib.PLAYERS_SCREEN.containsKey(client.player) && Eggolib.PLAYERS_SCREEN.get(client.player).equals(screenState)) {
			return;
		}

		Eggolib.PLAYERS_SCREEN.put(client.player, screenState);
		ClientPlayNetworking.send(new SyncScreenStatePacket(screenState));

	}

	public static void setPerspective(MinecraftClient minecraftClient, EggolibPerspective eggolibPerspective) {

		if (minecraftClient.player == null) {
			return;
		}

		switch (eggolibPerspective) {
			case FIRST_PERSON -> minecraftClient.options.setPerspective(Perspective.FIRST_PERSON);
			case THIRD_PERSON_BACK -> minecraftClient.options.setPerspective(Perspective.THIRD_PERSON_BACK);
			case THIRD_PERSON_FRONT -> minecraftClient.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
		}

	}

	public static void getPerspective(MinecraftClient minecraftClient) {
		getPerspective(minecraftClient, minecraftClient.options.getPerspective());
	}

	public static void getPerspective(MinecraftClient minecraftClient, Perspective currentPerspective) {

		if (minecraftClient.player == null) {
			return;
		}

		String currentEggolibPerspective = switch (currentPerspective) {
			case FIRST_PERSON -> EggolibPerspective.FIRST_PERSON.toString();
			case THIRD_PERSON_BACK -> EggolibPerspective.THIRD_PERSON_BACK.toString();
			case THIRD_PERSON_FRONT -> EggolibPerspective.THIRD_PERSON_FRONT.toString();
		};

		if (Eggolib.PLAYERS_PERSPECTIVE.get(minecraftClient.player) != null && Eggolib.PLAYERS_PERSPECTIVE.get(minecraftClient.player).equalsIgnoreCase(currentEggolibPerspective)) {
			return;
		}

		Eggolib.PLAYERS_PERSPECTIVE.put(minecraftClient.player, currentEggolibPerspective);
		ClientPlayNetworking.send(new SyncPerspectivePacket(currentEggolibPerspective));

	}

	public static boolean shouldPreventKey(KeyBinding keyBinding, MinecraftClient client) {

		String keyBindingName = keyBinding.getTranslationKey();
		List<PreventKeyUsePower> powers = PowerHolderComponent.getPowers(client.player, PreventKeyUsePower.class)
			.stream()
			.filter(p -> p.doesApply(keyBinding))
			.toList();

		if (powers.isEmpty()) {
			return false;
		}


		List<PreventKeyUsePower> powersToSync = powers
			.stream()
			.filter(
				p -> p.getSpecifiedKeys()
					.stream()
					.filter(k -> k.key.equals(keyBindingName))
					.anyMatch(k -> k.continuous || !EggolibClient.PREVENTED_KEY_BINDINGS.getOrDefault(keyBinding, false))
			)
			.toList();

		if (!powersToSync.isEmpty()) {
			powersToSync.forEach(p -> p.executeActions(keyBindingName));
			ClientPlayNetworking.send(new SyncPreventedKeyPacket(keyBindingName, powersToSync.stream().map(p -> p.getType().getIdentifier()).toList()));
		}

		return true;

	}

}
