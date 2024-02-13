package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class ItemCooldownCondition {

	public static boolean condition(SerializableData.Instance data, ItemStack stack) {

		Entity stackHolder = stack.getHolder();
		if (stack.isEmpty() || !(stackHolder instanceof PlayerEntity player)) {
			return false;
		}

		ItemCooldownManager cooldownManager = player.getItemCooldownManager();
		Comparison comparison = data.get("comparison");

		float compareTo = data.get("compare_to");
		return comparison.compare(cooldownManager.getCooldownProgress(stack.getItem(), 0.0f), compareTo);

	}

	public static ConditionFactory<ItemStack> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("item_cooldown"),
			new SerializableData()
				.add("comparison", ApoliDataTypes.COMPARISON)
				.add("compare_to", SerializableDataTypes.FLOAT),
			ItemCooldownCondition::condition
		);
	}

}
