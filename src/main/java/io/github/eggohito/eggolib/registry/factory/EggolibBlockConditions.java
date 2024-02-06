package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.block.AirCondition;
import io.github.eggohito.eggolib.condition.block.CommandCondition;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.registry.Registry;

public class EggolibBlockConditions {

	public static void register() {
		register(AirCondition.getFactory());
		register(CommandCondition.getFactory());
	}

	public static ConditionFactory<CachedBlockPosition> register(ConditionFactory<CachedBlockPosition> conditionFactory) {
		return Registry.register(ApoliRegistries.BLOCK_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
	}

}
