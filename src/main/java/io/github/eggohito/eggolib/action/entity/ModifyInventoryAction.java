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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;

import static io.github.eggohito.eggolib.util.InventoryUtils.*;

public class ModifyInventoryAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity playerEntity)) return;

        InventoryType inventoryType = data.get("inventory_type");

        switch (inventoryType) {
            case ENDER_CHEST:
                EnderChestInventory enderChestInventory = playerEntity.getEnderChestInventory();
                modify(data, playerEntity, enderChestInventory);
                break;
            case PLAYER:
                PlayerInventory playerInventory = playerEntity.getInventory();
                modify(data, playerEntity, playerInventory);
                break;
            case POWER:
                if (!data.isPresent("power")) return;

                PowerType<?> targetPowerType = data.get("power");
                Power targetPower = PowerHolderComponent.KEY.get(playerEntity).getPower(targetPowerType);

                if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
                    modify(data, playerEntity, eggolibInventoryPower);
                }
                else if (targetPower instanceof InventoryPower inventoryPower) {
                    modify(data, playerEntity, inventoryPower);
                }
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("modify_inventory"),
            new SerializableData()
                .add("inventory_type", EggolibDataTypes.INVENTORY_TYPE, InventoryType.PLAYER)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_action", ApoliDataTypes.ITEM_ACTION)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slots", SerializableDataTypes.INTS, null)
                .add("power", ApoliDataTypes.POWER_TYPE, null),
            ModifyInventoryAction::action
        );
    }
}
