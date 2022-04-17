package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.MathUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;

import static io.github.eggohito.eggolib.util.MathUtil.*;

public class CalculateResourceAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof LivingEntity livingEntity)) return;

        PowerHolderComponent phc = PowerHolderComponent.KEY.get(livingEntity);

        PowerType<?> targetPowerType = data.get("target");
        Power targetPower = phc.getPower(targetPowerType);

        PowerType<?> sourcePowerType = data.get("source");
        Power sourcePower = phc.getPower(sourcePowerType);

        MathOperation mathOperation = data.get("operation");

        Pair<Integer, Integer> result = calculate(targetPower, mathOperation, sourcePower);
        syncPowers(targetPower, sourcePower, result);
    }

    private static Pair<Integer, Integer> calculate(Power targetPower, MathOperation mathOperation, Power sourcePower) {

        int a = 0;
        int b = 0;

        if (targetPower instanceof VariableIntPower targetResource) a = targetResource.getValue();
        else if (targetPower instanceof CooldownPower targetCooldown) a = targetCooldown.getRemainingTicks();

        if (sourcePower instanceof VariableIntPower sourceResource) b = sourceResource.getValue();
        else if (sourcePower instanceof CooldownPower sourceCooldown) b = sourceCooldown.getRemainingTicks();

        return MathUtil.operate(a, mathOperation, b);
    }

    private static void syncPowers(Power targetPower, Power sourcePower, Pair<Integer, Integer> result) {
        if (targetPower instanceof VariableIntPower targetResource) targetResource.setValue(result.getLeft());
        else if (targetPower instanceof CooldownPower targetCooldown) targetCooldown.setCooldown(result.getLeft());

        if (sourcePower instanceof VariableIntPower sourceResource) sourceResource.setValue(result.getRight());
        else if (sourcePower instanceof CooldownPower sourceCooldown) sourceCooldown.setCooldown(result.getRight());
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("calculate_resource"),
            new SerializableData()
                .add("target", ApoliDataTypes.POWER_TYPE)
                .add("operation", EggolibDataTypes.MATH_OPERATION, MathOperation.ADD)
                .add("source", ApoliDataTypes.POWER_TYPE),
            CalculateResourceAction::action
        );
    }
}
