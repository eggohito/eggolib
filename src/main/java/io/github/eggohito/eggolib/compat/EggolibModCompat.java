package io.github.eggohito.eggolib.compat;

import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import java.util.List;

public abstract class EggolibModCompat implements IEggolibModCompat {

    public final String getModName(ModContainer modContainer) {
        return modContainer.getMetadata().getName();
    }

    public final String getModAuthors(ModContainer modContainer) {

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

}
