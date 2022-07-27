package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.InventoryUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EggolibInventoryUtil;
import net.minecraft.entity.Entity;

public class InventoryCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        InventoryUtil.InventoryType inventoryType = data.get("inventory_type");
        int[] matches = {0};

        switch (inventoryType) {
            case INVENTORY -> matches[0] = EggolibInventoryUtil.checkInventory(data, entity, null);
            case POWER -> {

                if (!data.isPresent("power")) return false;

                PowerType<?> targetPowerType = data.get("power");
                PowerHolderComponent.KEY.maybeGet(entity).ifPresent(
                    powerHolderComponent -> {

                        Power targetPower = powerHolderComponent.getPower(targetPowerType);
                        if (!(targetPower instanceof InventoryPower inventoryPower)) return;

                        matches[0] = EggolibInventoryUtil.checkInventory(data, entity, inventoryPower);

                    }
                );

            }
        }

        return matches[0] > 0;

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("inventory"),
            new SerializableData()
                .add("inventory_type", SerializableDataType.enumValue(InventoryUtil.InventoryType.class), InventoryUtil.InventoryType.INVENTORY)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slots", ApoliDataTypes.ITEM_SLOTS, null)
                .add("slot", ApoliDataTypes.ITEM_SLOT, null)
                .add("power", ApoliDataTypes.POWER_TYPE, null),
            InventoryCondition::condition
        );
    }

}
