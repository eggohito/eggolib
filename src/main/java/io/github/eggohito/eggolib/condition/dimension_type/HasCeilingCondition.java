package io.github.eggohito.eggolib.condition.dimension_type;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class HasCeilingCondition {

	public static boolean condition(SerializableData.Instance data, RegistryEntry<DimensionType> dimensionTypeRegistryEntry) {
		return dimensionTypeRegistryEntry.value().hasCeiling();
	}

	public static ConditionFactory<RegistryEntry<DimensionType>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("has_ceiling"),
			new SerializableData(),
			HasCeilingCondition::condition
		);
	}

}
