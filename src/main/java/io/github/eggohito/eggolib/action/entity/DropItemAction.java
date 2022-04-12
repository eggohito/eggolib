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
import io.github.eggohito.eggolib.util.EggolibDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;

import static io.github.eggohito.eggolib.util.InventoryUtils.*;

public class DropItemAction {

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity playerEntity)) return;

        InventoryType inventoryType = data.get("inventory_type");

        switch (inventoryType) {
            case ENDER_CHEST:
                EnderChestInventory enderChestInventory = playerEntity.getEnderChestInventory();
                drop(data, playerEntity, enderChestInventory);
                break;
            case PLAYER:
                PlayerInventory playerInventory = playerEntity.getInventory();
                drop(data, playerEntity, playerInventory);
                break;
            case POWER:
                if (!data.isPresent("power")) return;

                PowerType<?> targetPowerType = data.get("power");
                Power targetPower = PowerHolderComponent.KEY.get(playerEntity).getPower(targetPowerType);

                if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
                    drop(data, playerEntity, eggolibInventoryPower);
                }
                else if (targetPower instanceof InventoryPower inventoryPower) {
                    drop(data, playerEntity, inventoryPower);
                }
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("drop_item"),
            new SerializableData()
                .add("inventory_type", EggolibDataTypes.INVENTORY_TYPE, InventoryType.PLAYER)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slots", SerializableDataTypes.INTS, null)
                .add("power", ApoliDataTypes.POWER_TYPE, null)
                .add("throw_randomly", SerializableDataTypes.BOOLEAN, false)
                .add("retain_ownership", SerializableDataTypes.BOOLEAN, true),
            DropItemAction::action
        );
    }
}
