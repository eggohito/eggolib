package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.bientity.*;
import io.github.eggohito.eggolib.condition.meta.ChanceCondition;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class EggolibBiEntityConditions {

	public static void register() {
		register(ChanceCondition.getFactory());
		register(CollidingCondition.getFactory());
		register(CompareScoreCondition.getFactory());
		register(EqualCondition.getFactory());
		register(HasMatchingTagCondition.getFactory());
		register(OwnerCondition.getFactory());
	}

	public static ConditionFactory<Pair<Entity, Entity>> register(ConditionFactory<Pair<Entity, Entity>> conditionFactory) {
		return Registry.register(ApoliRegistries.BIENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
	}

}
