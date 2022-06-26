package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.*;
import net.minecraft.util.registry.Registry;

public class EggolibPowers {

    public static void register() {
        register(ActionOnBlockPlacePower.getFactory());
        register(EggolibInventoryPower.getFactory());
        register(EggolibModifyDamageDealtPower.getFactory());
        register(EggolibModifyDamageTakenPower.getFactory());
        register(EggolibModifyProjectileDamagePower.getFactory());
        register(PreventBlockPlacePower.getFactory());
    }

    private static void register(PowerFactory<?> serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }
}
