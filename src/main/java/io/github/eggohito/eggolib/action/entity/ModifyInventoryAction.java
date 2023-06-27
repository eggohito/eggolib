package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.InventoryType;
import io.github.eggohito.eggolib.util.InventoryUtil;
import io.github.eggohito.eggolib.util.ProcessMode;
import net.minecraft.entity.Entity;

import java.util.EnumSet;
import java.util.Set;

public class ModifyInventoryAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		Set<InventoryType> inventoryTypes = EnumSet.noneOf(InventoryType.class);
		ProcessMode processMode = data.get("process_mode");
		int limit = data.get("limit");

		data.ifPresent("inventory_type", inventoryTypes::add);
		data.ifPresent("inventory_types", inventoryTypes::addAll);

		if (inventoryTypes.contains(InventoryType.INVENTORY)) {
			InventoryUtil.modifyInventory(data, entity, null, processMode.getProcessor(), limit);
		}

		if (inventoryTypes.contains(InventoryType.POWER)) {

			PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
			if (component == null) {
				return;
			}

			PowerType<?> powerType = data.get("power");
			if (powerType == null) {
				return;
			}

			Power power = component.getPower(powerType);
			if (power instanceof InventoryPower inventoryPower) {
				InventoryUtil.modifyInventory(data, entity, inventoryPower, processMode.getProcessor(), limit);
			}

		}

	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("modify_inventory"),
			new SerializableData()
				.add("inventory_type", EggolibDataTypes.INVENTORY_TYPE, InventoryType.INVENTORY)
				.add("inventory_types", EggolibDataTypes.INVENTORY_TYPE_SET, EnumSet.noneOf(InventoryType.class))
				.add("process_mode", EggolibDataTypes.PROCESS_MODE, ProcessMode.STACKS)
				.add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
				.add("item_action", ApoliDataTypes.ITEM_ACTION, null)
				.add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
				.add("slot", ApoliDataTypes.ITEM_SLOT, null)
				.add("slots", ApoliDataTypes.ITEM_SLOTS, null)
				.add("power", ApoliDataTypes.POWER_TYPE, null)
				.add("limit", SerializableDataTypes.INT, 0),
			ModifyInventoryAction::action
		);
	}

}
