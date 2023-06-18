package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.ItemStack;

public class FuelCondition {

	public static boolean condition(SerializableData.Instance data, ItemStack stack) {

		Integer fuelTime = FuelRegistry.INSTANCE.get(stack.getItem());
		Comparison comparison = data.get("comparison");
		Integer compareTo = data.get("compare_to");

		boolean hasFuelTime = fuelTime != null;

		if (comparison == null || compareTo == null) {
			return hasFuelTime;
		}

		return hasFuelTime
			&& comparison.compare(fuelTime, compareTo);

	}

	public static ConditionFactory<ItemStack> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("fuel"),
			new SerializableData()
				.add("comparison", ApoliDataTypes.COMPARISON, null)
				.add("compare_to", SerializableDataTypes.INT, null),
			FuelCondition::condition
		);
	}

}
