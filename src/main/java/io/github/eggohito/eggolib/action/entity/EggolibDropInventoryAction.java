package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EggolibInventoryUtil;
import net.minecraft.entity.Entity;

public class EggolibDropInventoryAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        InventoryUtil.InventoryType inventoryType = data.get("inventory_type");

        switch (inventoryType) {
            case INVENTORY -> EggolibInventoryUtil.dropInventory(data, entity, null);
            case POWER -> {

                if (!data.isPresent("power")) return;

                PowerType<?> targetPowerType = data.get("power");
                PowerHolderComponent.KEY.maybeGet(entity).ifPresent(
                    powerHolderComponent -> {

                        Power targetPower = powerHolderComponent.getPower(targetPowerType);
                        if (!(targetPower instanceof InventoryPower inventoryPower)) return;

                        EggolibInventoryUtil.dropInventory(data, entity, inventoryPower);

                    }
                );

            }
        }

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("drop_inventory"),
            new SerializableData()
                .add("inventory_type", SerializableDataType.enumValue(InventoryUtil.InventoryType.class), InventoryUtil.InventoryType.INVENTORY)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slots", ApoliDataTypes.ITEM_SLOTS, null)
                .add("slot", ApoliDataTypes.ITEM_SLOT, null)
                .add("power", ApoliDataTypes.POWER_TYPE, null)
                .add("throw_randomly", SerializableDataTypes.BOOLEAN, false)
                .add("retain_ownership", SerializableDataTypes.BOOLEAN, true)
                .add("amount", SerializableDataTypes.INT, 0),
            EggolibDropInventoryAction::action
        );
    }

}
