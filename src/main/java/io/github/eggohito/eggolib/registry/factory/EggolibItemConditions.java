package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.item.BlockItemCondition;
import io.github.eggohito.eggolib.condition.item.ToolCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class EggolibItemConditions {

	public static void register() {
		register(BlockItemCondition.getFactory());
		register(ToolCondition.getFactory());
	}

	public static ConditionFactory<Pair<World, ItemStack>> register(ConditionFactory<Pair<World, ItemStack>> conditionFactory) {
		return Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
	}

}
