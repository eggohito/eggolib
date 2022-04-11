package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EggolibDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import static io.github.eggohito.eggolib.util.MathUtils.*;

public class CalculateResourceAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return;

        PowerHolderComponent phc = PowerHolderComponent.KEY.get(livingEntity);

        PowerType<?> targetPowerType = data.get("target");
        Power targetPower = phc.getPower(targetPowerType);
        PowerType<?> sourcePowerType = data.get("source");
        Power sourcePower = phc.getPower(sourcePowerType);

        Operation operation = data.get("operation");

        operatePowers(targetPower, operation, sourcePower);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("calculate_resource"),
            new SerializableData()
                .add("target", ApoliDataTypes.POWER_TYPE)
                .add("operation", EggolibDataTypes.MATH_OPERATION, Operation.ADD)
                .add("source", ApoliDataTypes.POWER_TYPE),
            CalculateResourceAction::action
        );
    }
}
