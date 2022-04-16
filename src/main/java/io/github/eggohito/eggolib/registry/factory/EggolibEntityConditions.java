package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public class EggolibEntityConditions {

    public static void register() {
        register(InGuiCondition.getFactory());
        register(PermissionLevelCondition.getFactory());
        register(PerspectiveCondition.getFactory());
        register(ScoreboardCondition.getFactory());
    }

    private static void register(ConditionFactory<Entity> conditionFactory) {
        Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
