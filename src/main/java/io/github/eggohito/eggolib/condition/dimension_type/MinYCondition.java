package io.github.eggohito.eggolib.condition.dimension_type;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class MinYCondition {

	public static boolean condition(SerializableData.Instance data, RegistryEntry<DimensionType> dimensionTypeRegistryEntry) {

		Comparison comparison = data.get("comparison");
		int specifiedValue = data.getInt("compare_to");

		return comparison.compare(dimensionTypeRegistryEntry.value().minY(), specifiedValue);

	}

	public static ConditionFactory<RegistryEntry<DimensionType>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("min_y"),
			new SerializableData()
				.add("comparison", ApoliDataTypes.COMPARISON)
				.add("compare_to", SerializableDataTypes.INT),
			MinYCondition::condition
		);
	}

}
