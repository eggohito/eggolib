package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.registry.factory.EggolibEntityActions;
import io.github.eggohito.eggolib.registry.factory.EggolibEntityConditions;
import io.github.eggohito.eggolib.registry.factory.EggolibItemConditions;
import io.github.eggohito.eggolib.registry.factory.EggolibPowers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Eggolib implements ModInitializer {

    public static final String MOD_ID = "eggolib";
    public static final Logger LOGGER = LoggerFactory.getLogger(Eggolib.class);
    public static String VERSION = "";

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

        EggolibPowers.register();
        EggolibEntityActions.register();
        EggolibEntityConditions.register();
        EggolibItemConditions.register();

        LOGGER.info(String.format("[eggolib] Eggolib %s has been initialized. Egg!", VERSION));
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }
}
