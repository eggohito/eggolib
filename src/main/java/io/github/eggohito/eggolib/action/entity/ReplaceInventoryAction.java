package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.EggolibInventoryUtil.InventoryType;
import net.minecraft.entity.Entity;

import static io.github.eggohito.eggolib.util.EggolibInventoryUtil.*;

public class ReplaceInventoryAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        InventoryType inventoryType = data.get("inventory_type");

        switch (inventoryType) {
            case INVENTORY:
                replaceInventory(data, entity, null);
                break;
            case POWER:
                if (!data.isPresent("power")) return;

                PowerType<?> targetPowerType = data.get("power");
                if (targetPowerType == null) return;

                PowerHolderComponent.KEY
                    .maybeGet(entity)
                    .ifPresent(
                        powerHolderComponent -> {
                            Power targetPower = powerHolderComponent.getPower(targetPowerType);
                            replaceInventory(data, entity, targetPower);
                        }
                    );

                break;
        }

    }

    public static ActionFactory<Entity> getFactory() {

        return new ActionFactory<>(
            Eggolib.identifier("replace_inventory"),
            new SerializableData()
                .add("inventory_type", EggolibDataTypes.INVENTORY_TYPE, InventoryType.INVENTORY)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slot", EggolibDataTypes.ITEM_SLOT, null)
                .add("slots", EggolibDataTypes.ITEM_SLOTS, null)
                .add("power", ApoliDataTypes.POWER_TYPE, null)
                .add("stack", SerializableDataTypes.ITEM_STACK),
            ReplaceInventoryAction::action
        );

    }

}
