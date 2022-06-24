package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.networking.EggolibPacketsC2S;
import io.github.eggohito.eggolib.registry.factory.*;
import io.github.eggohito.eggolib.util.EggolibConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Eggolib implements ModInitializer {

    public static EggolibConfig CONFIG;

    public static final String MOD_ID = "eggolib";
    public static final Logger LOGGER = LoggerFactory.getLogger(Eggolib.class);

    public static String VERSION = "";
    public static int[] SEMVER;

    public static HashMap<PlayerEntity, Boolean> PLAYERS_IN_SCREEN = new HashMap<>();
    public static HashMap<PlayerEntity, String> PLAYERS_PERSPECTIVE = new HashMap<>();

    @Override
    public void onInitialize() {

        //  Get the semantic version of the mod
        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(
            modContainer -> {

                VERSION = modContainer.getMetadata().getVersion().getFriendlyString();

                if (VERSION.contains("+")) {
                    VERSION = VERSION.split("\\+")[0];
                }
                if (VERSION.contains("-")) {
                    VERSION = VERSION.split("-")[0];
                }

                String[] splitVersion = VERSION.split("\\.");
                SEMVER = new int[splitVersion.length];

                for (int i = 0; i < SEMVER.length; i++) {
                    SEMVER[i] = Integer.parseInt(splitVersion[i]);
                }

            }
        );

        // TODO: Finish implementing the 'concat' command (alias is not final)
//        CommandRegistrationCallback.EVENT.register(
//            (commandDispatcher, dedicated) -> ConcatCommand.register(commandDispatcher)
//        );

        //  Register the partitioned config
        AutoConfig.register(EggolibConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        CONFIG = AutoConfig.getConfigHolder(EggolibConfig.class).getConfig();

        Eggolib.LOGGER.warn(
            String.format("[%1$s] Should %1$s perform a version check? %2$s", MOD_ID, CONFIG.server.performVersionCheck ? "Yes." : "No.")
        );

        //  Register the packets
        EggolibPacketsC2S.register();

        //  Register the action/condition/power type factories
        EggolibBlockActions.register();
        EggolibBlockConditions.register();
        EggolibDamageConditions.register();
        EggolibEntityActions.register();
        EggolibEntityConditions.register();
        EggolibItemConditions.register();
        EggolibPowers.register();

        LOGGER.info("[{}] Version {} has been initialized! Egg!", MOD_ID, VERSION);

        ServerPlayConnectionEvents.DISCONNECT.register(
            (serverPlayNetworkHandler, minecraftServer) -> {
                PLAYERS_IN_SCREEN.clear();
                PLAYERS_PERSPECTIVE.clear();
            }
        );

    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

}
