package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.power.factory.condition.MetaConditions;
import io.github.eggohito.eggolib.condition.dimension_type.*;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.registry.EggolibRegistries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class EggolibDimensionTypeConditions {

	public static void register() {

		//  Register Apoli's meta condition types
		MetaConditions.register(EggolibDataTypes.DIMENSION_TYPE_CONDITION, EggolibDimensionTypeConditions::register);

		//  Register eggolib's dimension type condition types
		register(AmbientLightCondition.getFactory());
		register(BedWorksCondition.getFactory());
		register(CoordinateScaleCondition.getFactory());
		register(EffectsCondition.getFactory());
		register(FixedTimeCondition.getFactory());
		register(HasCeilingCondition.getFactory());
		register(HasRaidsCondition.getFactory());
		register(HasSkylightCondition.getFactory());
		register(HeightCondition.getFactory());
		register(InfiniburnCondition.getFactory());
		register(IsNaturalCondition.getFactory());
		register(IsPiglinSafeCondition.getFactory());
		register(IsUltrawarmCondition.getFactory());
		register(LogicalHeightCondition.getFactory());
		register(MinYCondition.getFactory());
		register(MonsterSpawnBlockLightLimitCondition.getFactory());
		register(MonsterSpawnLightLevelCondition.getFactory());
		register(RespawnAnchorWorksCondition.getFactory());

	}

	public static ConditionFactory<RegistryEntry<DimensionType>> register(ConditionFactory<RegistryEntry<DimensionType>> conditionFactory) {
		return Registry.register(EggolibRegistries.DIMENSION_TYPE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
	}

}
