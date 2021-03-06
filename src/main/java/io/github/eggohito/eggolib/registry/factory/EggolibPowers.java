package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.power.*;
import net.minecraft.util.registry.Registry;

public class EggolibPowers {

    public static void register() {
        register(ActionOnBlockPlacePower.getFactory());
        register(EggolibInvisibilityPower.getFactory());
        register(EggolibPreventItemUsePower.getFactory());
        register(ModelFlipPower.getFactory());
        register(ModifyHurtTicksPower.getFactory());
        register(PreventBlockPlacePower.getFactory());
    }

    private static void register(PowerFactory<?> serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
