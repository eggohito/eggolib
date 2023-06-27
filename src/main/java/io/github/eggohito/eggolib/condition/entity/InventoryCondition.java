package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.InventoryType;
import io.github.eggohito.eggolib.util.InventoryUtil;
import io.github.eggohito.eggolib.util.ProcessMode;
import net.minecraft.entity.Entity;

import java.util.EnumSet;

public class InventoryCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		EnumSet<InventoryType> inventoryTypes = EnumSet.noneOf(InventoryType.class);
		ProcessMode processMode = data.get("process_mode");
		Comparison comparison = data.get("comparison");

		int compareTo = data.get("compare_to");
		int matches = 0;

		data.ifPresent("inventory_type", inventoryTypes::add);
		data.ifPresent("inventory_types", inventoryTypes::addAll);

		if (inventoryTypes.contains(InventoryType.INVENTORY)) {
			matches += InventoryUtil.checkInventory(data, entity, null, processMode.getProcessor());
		}

		powerTest:
		if (inventoryTypes.contains(InventoryType.POWER)) {

			PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
			if (component == null) {
				break powerTest;
			}

			PowerType<?> targetPowerType = data.get("power");
			if (targetPowerType == null) {
				break powerTest;
			}

			Power targetPower = component.getPower(targetPowerType);
			if (targetPower instanceof InventoryPower inventoryPower) {
				matches += InventoryUtil.checkInventory(data, entity, inventoryPower, processMode.getProcessor());
			}

		}

		return comparison.compare(matches, compareTo);

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("inventory"),
			new SerializableData()
				.add("inventory_type", EggolibDataTypes.INVENTORY_TYPE, InventoryType.INVENTORY)
				.add("inventory_types", EggolibDataTypes.INVENTORY_TYPE_SET, EnumSet.noneOf(InventoryType.class))
				.add("process_mode", EggolibDataTypes.PROCESS_MODE, ProcessMode.STACKS)
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
