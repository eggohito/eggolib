package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.bientity.HasMatchingTagCondition;
import io.github.eggohito.eggolib.condition.meta.ChanceCondition;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EggolibBiEntityConditions {

    public static void register() {
        register(ChanceCondition.getFactory());
        register(HasMatchingTagCondition.getFactory());
    }

    private static void register(ConditionFactory<Pair<Entity, Entity>> conditionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
