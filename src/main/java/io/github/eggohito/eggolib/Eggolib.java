package io.github.eggohito.eggolib;

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

        LOGGER.info(String.format("eggolib %s has been initialized!", VERSION));
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }
}
