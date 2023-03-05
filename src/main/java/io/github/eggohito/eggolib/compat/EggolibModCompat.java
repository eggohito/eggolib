package io.github.eggohito.eggolib.compat;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class EggolibModCompat {

    public static String getModName(ModContainer modContainer) {
        ModMetadata modMetadata = modContainer.getMetadata();
        return modMetadata.getName();
    }

    public static String getModAuthors(ModContainer modContainer) {

        ModMetadata modMetadata = modContainer.getMetadata();
        StringBuilder stringBuilder = new StringBuilder();
        List<Person> modAuthors = modMetadata.getAuthors().stream().toList();

        String separator = "";
        for (int i = 0; i < modAuthors.size(); i++) {
            if (modAuthors.size() > 1 && i == modAuthors.size() - 1) separator = " and ";
            stringBuilder
                .append(separator)
                .append(modAuthors.get(i).getName());
            separator = ", ";
        }

        return stringBuilder.toString();

    }

    @Environment(EnvType.CLIENT)
    public static void addScreenMapping(String name, Class<? extends Screen> clazz) {
        ClassDataRegistry
            .getOrCreate(ClassUtil.castClass(Screen.class), "Screen")
            .addMapping(name, clazz);
    }

}
