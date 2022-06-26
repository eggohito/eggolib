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

    public static EggolibConfig config;

    public static final String MOD_ID = "eggolib";
    public static final Logger LOGGER = LoggerFactory.getLogger(Eggolib.class);

    public static String version = "";
    public static int[] semanticVersion;

    public static HashMap<PlayerEntity, Boolean> playersInScreen = new HashMap<>();
    public static HashMap<PlayerEntity, String> playersPerspective = new HashMap<>();

    @Override
    public void onInitialize() {

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

        // TODO: Finish implementing the 'concat' command (alias is not final)
//        CommandRegistrationCallback.EVENT.register(
//            (commandDispatcher, dedicated) -> ConcatCommand.register(commandDispatcher)
//        );

        //  Register the partitioned config
        AutoConfig.register(EggolibConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
        config = AutoConfig.getConfigHolder(EggolibConfig.class).getConfig();

        Eggolib.LOGGER.warn(
            String.format("[%1$s] Should %1$s perform a version check? %2$s", MOD_ID, config.server.performVersionCheck ? "Yes." : "No.")
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

        LOGGER.info("[{}] Version {} has been initialized! Egg!", MOD_ID, version);

        ServerPlayConnectionEvents.DISCONNECT.register(
            (serverPlayNetworkHandler, minecraftServer) -> {
                playersInScreen.clear();
                playersPerspective.clear();
            }
        );

    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

}
