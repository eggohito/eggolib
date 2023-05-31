package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;

import java.util.Set;

public class EnchantmentCondition {

	public static boolean condition(SerializableData.Instance data, ItemStack itemStack) {

		Comparison comparison = data.get("comparison");
		int compareTo = data.getInt("compare_to");

		if (data.isPresent("enchantment")) {

			Enchantment specifiedEnchantment = data.get("enchantment");

			int specifiedEnchantmentLevel = EnchantmentHelper.getLevel(specifiedEnchantment, itemStack);
			return comparison.compare(specifiedEnchantmentLevel, compareTo);

		} else {
			Set<Enchantment> enchantments = EnchantmentHelper.get(itemStack).keySet();
			return comparison.compare(enchantments.size(), compareTo);
		}

	}

	public static ConditionFactory<ItemStack> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("enchantment"),
			new SerializableData()
				.add("enchantment", SerializableDataTypes.ENCHANTMENT, null)
				.add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN_OR_EQUAL)
				.add("compare_to", SerializableDataTypes.INT, 1),
			EnchantmentCondition::condition
		);
	}

}
