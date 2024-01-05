package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.access.InventoryHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;

public class OpenInventoryAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
		if (component == null) {
			return;
		}

		PowerType<?> targetPowerType = data.get("power");
		if (targetPowerType == null) {

			if (entity instanceof InventoryHolder inventoryHolder) {
				inventoryHolder.eggolib$openInventory();
			}

			return;

		}

		Power targetPower = component.getPower(targetPowerType);
		if (entity instanceof PlayerEntity playerEntity && targetPower instanceof InventoryPower inventoryPower) {
			playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory(inventoryPower.getContainerScreen(), inventoryPower.getContainerTitle()));
		}

	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("open_inventory"),
			new SerializableData()
				.add("power", ApoliDataTypes.POWER_TYPE, null),
			OpenInventoryAction::action
		);
	}

}
