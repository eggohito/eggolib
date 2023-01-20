package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class ModifyMouseSensitivityPower extends ValueModifyingPower {

    public ModifyMouseSensitivityPower(PowerType<?> powerType, LivingEntity livingEntity) {
        super(powerType, livingEntity);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("modify_mouse_sensitivity"),
            new SerializableData()
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null),
            data -> (powerType, livingEntity) -> {

                ModifyMouseSensitivityPower mmsp = new ModifyMouseSensitivityPower(
                    powerType,
                    livingEntity
                );

                data.ifPresent("modifier", mmsp::addModifier);
                data.<List<Modifier>>ifPresent("modifiers", mods -> mods.forEach(mmsp::addModifier));

                return mmsp;

            }
        ).allowCondition();
    }

}
