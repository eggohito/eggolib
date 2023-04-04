package io.github.eggohito.eggolib.compat;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class EggolibModCompatClient extends EggolibModCompat {

    private static final HashMap<String, Class<? extends Screen>> SCREEN_MAPPINGS = new HashMap<>();

    public static void addScreenMapping(String name, Class<? extends Screen> clazz) {
        ClassDataRegistry
            .getOrCreate(ClassUtil.castClass(Screen.class), "Screen")
            .addMapping(name, clazz);
        SCREEN_MAPPINGS.put(name, clazz);
    }

    public static String formatScreenMappings() {

        StringBuilder builder = new StringBuilder();
        SCREEN_MAPPINGS.forEach(
            (s, aClass) -> builder.append("\t- ").append(aClass.getName()).append("\t(mapped as \"").append(s).append("\")\n")
        );

        return builder.toString();

    }

}
