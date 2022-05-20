package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.networking.EggolibPacketsC2S;
import io.github.eggohito.eggolib.registry.factory.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Eggolib implements ModInitializer {

    public static final String MOD_ID = "eggolib";
    public static final Logger LOGGER = LoggerFactory.getLogger(Eggolib.class);
    public static String VERSION = "";
    public static HashMap<PlayerEntity, String> playerCurrentScreenHashMap = new HashMap<>();
    public static HashMap<PlayerEntity, String> playerCurrentPerspectiveHashMap = new HashMap<>();

    @Override
    public void onInitialize() {

        FabricLoader.getInstance().getModContainer(MOD_ID).ifPresent(
            modContainer -> {
                VERSION = modContainer.getMetadata().getVersion().getFriendlyString();
                if (VERSION.contains("+")) {
                    VERSION = VERSION.split("\\+")[0];
                }
                if (VERSION.contains("-")) {
                    VERSION = VERSION.split("-")[0];
                }
            }
        );

        // TODO: Finish implementing the 'concat' command (alias is not final)
//        CommandRegistrationCallback.EVENT.register(
//            (commandDispatcher, dedicated) -> ConcatCommand.register(commandDispatcher)
//        );

        EggolibPacketsC2S.register();

        EggolibBlockActions.register();
        EggolibBlockConditions.register();
        EggolibEntityActions.register();
        EggolibEntityConditions.register();
        EggolibItemConditions.register();
        EggolibPowers.register();

        LOGGER.info("[{}] Version {} has been initialized! Egg!", MOD_ID, VERSION);

        ServerPlayConnectionEvents.DISCONNECT.register(
            (serverPlayNetworkHandler, minecraftServer) -> {
                playerCurrentScreenHashMap.clear();
                playerCurrentPerspectiveHashMap.clear();
            }
        );

    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

}
