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
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import java.util.function.Consumer;

public class LoopAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof LivingEntity livingEntity)) return;

        PowerType<?> powerType = data.get("iterations");
        Power power = PowerHolderComponent.KEY.get(livingEntity).getPower(powerType);
        Consumer<Entity> beforeAction = data.get("before_action");
        Consumer<Entity> afterAction = data.get("after_action");
        Consumer<Entity> entityAction = data.get("action");
        int iterations = 0;

        if (power instanceof VariableIntPower resourcePower) iterations = resourcePower.getValue();
        else if (power instanceof CooldownPower cooldownPower) iterations = cooldownPower.getRemainingTicks();

        if (beforeAction != null) beforeAction.accept(livingEntity);
        for (int i = 0; i < iterations; i++) {
            entityAction.accept(livingEntity);
        }
        if (afterAction != null) afterAction.accept(livingEntity);

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("loop"),
            new SerializableData()
                .add("before_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("action", ApoliDataTypes.ENTITY_ACTION)
                .add("after_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("iterations", ApoliDataTypes.POWER_TYPE),
            LoopAction::action
        );
    }

}
