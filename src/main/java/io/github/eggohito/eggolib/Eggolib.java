package io.github.eggohito.eggolib;

import io.github.apace100.apoli.util.IdentifierAlias;
import io.github.eggohito.eggolib.compat.EggolibModCompat;
import io.github.eggohito.eggolib.data.EggolibClassData;
import io.github.eggohito.eggolib.integration.EggolibPowerIntegration;
import io.github.eggohito.eggolib.networking.EggolibPacketsC2S;
import io.github.eggohito.eggolib.registry.factory.*;
import io.github.eggohito.eggolib.util.ScreenState;
import io.github.eggohito.eggolib.util.config.EggolibConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

public class Eggolib implements ModInitializer {

	public static String version = "";
	public static int[] semanticVersion;

	public static EggolibConfig config;
	public static MinecraftServer minecraftServer;

	public static Supplier<Optional<DynamicRegistryManager>> registryManager = Optional::empty;

	public static final String MOD_ID = "eggolib";
	public static final Logger LOGGER = LoggerFactory.getLogger(Eggolib.class);

	public static final HashMap<PlayerEntity, ScreenState> PLAYERS_SCREEN = new HashMap<>();
	public static final HashMap<PlayerEntity, String> PLAYERS_PERSPECTIVE = new HashMap<>();

	@Override
	public void onInitialize() {

		//  Get the server
		ServerLifecycleEvents.SERVER_STARTED.register(server -> minecraftServer = server);

		//  Get the semantic version of the mod
		FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(
			modContainer -> {

				version = modContainer.getMetadata().getVersion().getFriendlyString();

				if (version.contains("+")) {
					version = version.split("\\+")[0];
				}
				if (version.contains("-")) {
					version = version.split("-")[0];
				}

				String[] splitVersion = version.split("\\.");
				semanticVersion = new int[splitVersion.length];

				for (int i = 0; i < semanticVersion.length; i++) {
					semanticVersion[i] = Integer.parseInt(splitVersion[i]);
				}

			}
		);

		//  Register the partitioned config
		AutoConfig.register(EggolibConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
		config = AutoConfig.getConfigHolder(EggolibConfig.class).getConfig();

		//  Add "apoli" as a namespace alias
		IdentifierAlias.addNamespaceAlias(MOD_ID, "apoli");

		//  Register the client-to-server packet receivers and class data registries
		EggolibPacketsC2S.register();
		EggolibClassData.register();

		//  Register the action/condition/power types
		EggolibBiEntityActions.register();
		EggolibBiEntityConditions.register();
		EggolibBlockActions.register();
		EggolibBlockConditions.register();
		EggolibDamageConditions.register();
		EggolibDimensionTypeConditions.register();
		EggolibEntityActions.register();
		EggolibEntityConditions.register();
		EggolibItemActions.register();
		EggolibItemConditions.register();
		EggolibLootConditions.register();
		EggolibPowers.register();

		//  Register callbacks used by some power types
		EggolibPowerIntegration.register();

		//  Notify client/server that eggolib has been initialized and if it should perform a version check
		LOGGER.info("[{}] Version {} has been initialized. Egg!", MOD_ID, version);
		LOGGER.warn(
			String.format("[%1$s] Should %1$s perform a version check? %2$s", MOD_ID, config.server.performVersionCheck ? "Yes." : "No.")
		);

		//  Initialize main compat. stuff
		FabricLoader.getInstance()
			.getEntrypointContainers("eggolib:compat", EggolibModCompat.class)
			.stream()
			.map(EntrypointContainer::getEntrypoint)
			.forEach(EggolibModCompat::init);

		//  Remove the player from the HashMaps upon them disconnecting
		ServerPlayConnectionEvents.DISCONNECT.register(
			(serverPlayNetworkHandler, minecraftServer) -> {
				PLAYERS_SCREEN.remove(serverPlayNetworkHandler.player);
				PLAYERS_PERSPECTIVE.remove(serverPlayNetworkHandler.player);
			}
		);

	}

	public static Identifier identifier(String path) {
		return new Identifier(MOD_ID, path);
	}

}
