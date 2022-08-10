package io.github.eggohito.eggolib;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.NamespaceAlias;
import io.github.eggohito.eggolib.command.EggolibCommand;
import io.github.eggohito.eggolib.networking.EggolibPacketsC2S;
import io.github.eggohito.eggolib.power.ActionOnBlockHitPower;
import io.github.eggohito.eggolib.registry.factory.*;
import io.github.eggohito.eggolib.util.EggolibConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Eggolib implements ModInitializer {

    public static String version = "";
    public static int[] semanticVersion;

    public static EggolibConfig config;
    public static MinecraftServer minecraftServer;

    public static final String MOD_ID = "eggolib";
    public static final Logger LOGGER = LoggerFactory.getLogger(Eggolib.class);

    public static final HashMap<PlayerEntity, Boolean> PLAYERS_IN_SCREEN = new HashMap<>();
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
        NamespaceAlias.addAlias(MOD_ID, "apoli");

        //  Register the packets
        EggolibPacketsC2S.register();

        //  Register the action/condition/power type factories
        EggolibBiEntityActions.register();
        EggolibBiEntityConditions.register();
        EggolibBlockActions.register();
        EggolibBlockConditions.register();
        EggolibDamageConditions.register();
        EggolibEntityActions.register();
        EggolibEntityConditions.register();
        EggolibItemActions.register();
        EggolibItemConditions.register();
        EggolibLootConditions.register();
        EggolibPowers.register();

        LOGGER.info("[{}] Version {} has been initialized. Egg!", MOD_ID, version);
        LOGGER.warn(
            String.format("[%1$s] Should %1$s perform a version check? %2$s", MOD_ID, config.server.performVersionCheck ? "Yes." : "No.")
        );

        //  Remove the key-value pair in the hashmaps for the player that disconnected
        ServerPlayConnectionEvents.DISCONNECT.register(
            (serverPlayNetworkHandler, minecraftServer) -> {
                PLAYERS_IN_SCREEN.remove(serverPlayNetworkHandler.player);
                PLAYERS_PERSPECTIVE.remove(serverPlayNetworkHandler.player);
            }
        );

        CommandRegistrationCallback.EVENT.register(
            (dispatcher, registryAccess, environment) -> EggolibCommand.register(dispatcher)
        );

        AttackBlockCallback.EVENT.register(
            (player, world, hand, pos, direction) -> {

                if (!player.isSpectator()) PowerHolderComponent.withPowers(
                    player,
                    ActionOnBlockHitPower.class,
                    aobhp -> aobhp.doesApply(world, pos, direction, null),
                    aobhp -> aobhp.executeActions(world, pos, direction, null)
                );

                return ActionResult.PASS;

            }
        );

    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }

}
