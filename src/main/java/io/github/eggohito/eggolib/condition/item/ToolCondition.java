package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.ToolType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.EnumSet;

public class ToolCondition {

	public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {

		EnumSet<ToolType> toolTypes = EnumSet.noneOf(ToolType.class);

		data.ifPresent("tool_type", toolTypes::add);
		data.ifPresent("tool_types", toolTypes::addAll);

		if (toolTypes.isEmpty()) {
			toolTypes.addAll(EnumSet.allOf(ToolType.class));
		}

		return toolTypes.stream().anyMatch(toolType -> toolType.matches(worldAndStack.getRight()));

	}

	public static ConditionFactory<Pair<World, ItemStack>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("tool"),
			new SerializableData()
				.add("tool_type", EggolibDataTypes.TOOL_TYPE, null)
				.add("tool_types", EggolibDataTypes.TOOL_TYPE_SET, null),
			ToolCondition::condition
		);
	}

}
