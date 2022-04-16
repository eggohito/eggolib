package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.power.*;
import net.minecraft.util.registry.Registry;

public class EggolibPowers {

    public static void register() {
        register(EggolibInventoryPower.getFactory());
        register(EggolibPreventItemUsePower.getFactory());
    }

    private static void register(PowerFactory<?> serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}