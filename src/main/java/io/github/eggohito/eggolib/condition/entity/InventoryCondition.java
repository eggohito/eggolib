package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.InventoryType;
import io.github.eggohito.eggolib.util.InventoryUtil;
import net.minecraft.entity.Entity;

public class InventoryCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		InventoryType inventoryType = data.get("inventory_type");
		Comparison comparison = data.get("comparison");

		int compareTo = data.get("compare_to");
		int matches = 0;

		if (inventoryType == InventoryType.INVENTORY) {
			matches += InventoryUtil.checkInventory(data, entity, null);
		}

		if (inventoryType == InventoryType.POWER) {

			PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
			if (component == null) {
				return false;
			}

			PowerType<?> targetPowerType = data.get("power");
			if (targetPowerType == null) {
				return false;
			}

			Power targetPower = component.getPower(targetPowerType);
			if (targetPower instanceof InventoryPower inventoryPower) {
				matches += InventoryUtil.checkInventory(data, entity, inventoryPower);
			}

		}

		return comparison.compare(matches, compareTo);

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("inventory"),
			new SerializableData()
				.add("inventory_type", SerializableDataType.enumValue(InventoryType.class), InventoryType.INVENTORY)
				.add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
				.add("slots", ApoliDataTypes.ITEM_SLOTS, null)
				.add("slot", ApoliDataTypes.ITEM_SLOT, null)
				.add("power", ApoliDataTypes.POWER_TYPE, null)
				.add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN)
				.add("compare_to", SerializableDataTypes.INT, 0),
			InventoryCondition::condition
		);
	}

}
