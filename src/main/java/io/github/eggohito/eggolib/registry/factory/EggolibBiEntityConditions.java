package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.bientity.*;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class EggolibBiEntityConditions {

	public static void register() {
		register(CollidingCondition.getFactory());
		register(CompareScoreCondition.getFactory());
		register(EqualCondition.getFactory());
		register(HasMatchingTagCondition.getFactory());
	}

	public static ConditionFactory<Pair<Entity, Entity>> register(ConditionFactory<Pair<Entity, Entity>> conditionFactory) {
		return Registry.register(ApoliRegistries.BIENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
	}

}
