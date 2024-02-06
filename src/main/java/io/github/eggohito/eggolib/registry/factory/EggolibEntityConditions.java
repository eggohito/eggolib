package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EggolibEntityConditions {

	public static void register() {
		register(BreakingBlockCondition.getFactory());
		register(CrawlingCondition.getFactory());
		register(DimensionCondition.getFactory());
		register(ExposedToWeatherCondition.getFactory());
		register(HasSpawnPointCondition.getFactory());
		register(HasTagCondition.getFactory());
		register(InBlockCondition.getFactory());
		register(InScreenCondition.getFactory());
		register(InTeamCondition.getFactory());
		register(MoonPhaseCondition.getFactory());
		register(PermissionLevelCondition.getFactory());
		register(PerspectiveCondition.getFactory());
	}

	public static ConditionFactory<Entity> register(ConditionFactory<Entity> conditionFactory) {
		return Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
	}

}
