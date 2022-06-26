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
import io.github.eggohito.eggolib.power.EggolibInventoryPower;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

import static io.github.eggohito.eggolib.util.EggolibInventoryUtil.*;

public class DropInventoryAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        InventoryType inventoryType = data.get("inventory_type");

        switch (inventoryType) {
            case INVENTORY:
                dropInventory(data, entity, null);
                break;
            case POWER:
                if (!data.isPresent("power") || !(entity instanceof LivingEntity livingEntity)) return;

                PowerType<?> targetPowerType = data.get("power");
                Power targetPower = PowerHolderComponent.KEY.get(livingEntity).getPower(targetPowerType);

                if (targetPower instanceof InventoryPower || targetPower instanceof EggolibInventoryPower) dropInventory(data, livingEntity, targetPower);
                break;
        }

    }

    public static ActionFactory<Entity> getFactory() {

        return new ActionFactory<>(
            Eggolib.identifier("drop_inventory"),
            new SerializableData()
                .add("inventory_type", EggolibDataTypes.INVENTORY_TYPE, InventoryType.INVENTORY)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
		        .add("slot", EggolibDataTypes.ITEM_SLOT, null)
                .add("slots", EggolibDataTypes.ITEM_SLOTS, null)
                .add("power", ApoliDataTypes.POWER_TYPE, null)
                .add("throw_randomly", SerializableDataTypes.BOOLEAN, false)
                .add("retain_ownership", SerializableDataTypes.BOOLEAN, true),
            DropInventoryAction::action
        );

    }

}
