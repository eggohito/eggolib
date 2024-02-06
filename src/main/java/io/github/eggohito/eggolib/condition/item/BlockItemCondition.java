package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class BlockItemCondition {

	public static boolean condition(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {
		return worldAndStack.getRight().getItem() instanceof BlockItem;
	}

	public static ConditionFactory<Pair<World, ItemStack>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("block_item"),
			new SerializableData(),
			BlockItemCondition::condition
		);
	}

}
