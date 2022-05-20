package io.github.eggohito.eggolib.compat;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

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

            stringBuilder
                .append(separator)
                .append(modAuthors.get(i).getName());

            if (i == modAuthors.size() - 1) separator = " and ";
            else separator = ", ";

        }

        return stringBuilder.toString();

    }

}
