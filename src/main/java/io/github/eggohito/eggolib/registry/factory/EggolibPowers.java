package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.power.*;
import net.minecraft.util.registry.Registry;

public class EggolibPowers {

    public static void register() {
        register(ActionOnBlockHitPower.getFactory());
        register(ActionOnBlockPlacePower.getFactory());
        register(ActionOnItemPickupPower.getFactory());
        register(ActionOnKeySequencePower.getFactory());
        register(EggolibInvisibilityPower.getFactory());
        register(EggolibPreventItemUsePower.getFactory());
        register(EggolibStartingEquipmentPower.getFactory());
        register(GameEventListenerPower.getFactory());
        register(ModelFlipPower.getFactory());
        register(ModifyBouncinessPower.getFactory());
        register(ModifyBreathingPower.getFactory());
        register(ModifyHurtTicksPower.getFactory());
        register(ModifyLabelRenderPower.getFactory());
        register(ModifyMouseSensitivityPower.getFactory());
        register(PreventBlockPlacePower.getFactory());
        register(PreventCriticalHitPower.getFactory());
        register(PreventItemPickupPower.getFactory());
    }

    private static void register(PowerFactory<?> serializer) {
        Registry.register(ApoliRegistries.POWER_FACTORY, serializer.getSerializerId(), serializer);
    }

}
