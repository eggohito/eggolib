package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.InventoryPowerAccessor;
import io.github.eggohito.eggolib.power.EggolibInventoryPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.screen.*;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;

public class OpenGuiAction {

    private enum GuiType {
        POWER,
        ENDER_CHEST,
        CRAFTING_TABLE
    }

    public static void action(SerializableData.Instance data, Entity entity) {
        if (!(entity instanceof PlayerEntity playerEntity)) return;

        GuiType guiType = data.get("gui_type");

        switch (guiType) {
            case POWER:
                if (!data.isPresent("power")) return;
                openInventoryPower(data, playerEntity);
                break;
            case ENDER_CHEST:
                openEnderChest(data, playerEntity);
                break;
            case CRAFTING_TABLE:
                openCraftingTable(data, playerEntity);
                break;
        }
    }

    private static void openInventoryPower(SerializableData.Instance data, PlayerEntity playerEntity) {

        PowerType<?> targetPowerType = data.get("power");
        Power targetPower = PowerHolderComponent.KEY.get(playerEntity).getPower(targetPowerType);

        ScreenHandlerFactory containerScreen = null;
        TranslatableText containerTitle = null;

        boolean valid = false;

        if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
            containerScreen = eggolibInventoryPower.getContainerScreen();
            containerTitle = eggolibInventoryPower.getContainerTitle();
            valid = true;
        }
        else if (targetPower instanceof InventoryPower inventoryPower) {
            containerScreen = ((InventoryPowerAccessor) inventoryPower).getFactory();
            containerTitle = ((InventoryPowerAccessor) inventoryPower).getContainerName();
            valid = true;
        }

        if (valid) playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory(containerScreen, containerTitle));
    }

    private static void openCraftingTable(SerializableData.Instance data, PlayerEntity playerEntity) {

        playerEntity.openHandledScreen(
            new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, player) -> new CraftingScreenHandler(
                    syncId,
                    inventory,
                    ScreenHandlerContext.create(
                        player.world,
                        player.getBlockPos()
                    )
                ),
                new TranslatableText("container.crafting")
            )
        );

        playerEntity.incrementStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
    }

    private static void openEnderChest(SerializableData.Instance data, PlayerEntity playerEntity) {

        EnderChestInventory enderChestInventory = playerEntity.getEnderChestInventory();

        playerEntity.openHandledScreen(
            new SimpleNamedScreenHandlerFactory(
                (syncId, inventory, player) -> GenericContainerScreenHandler.createGeneric9x3(
                    syncId,
                    inventory,
                    enderChestInventory
                ),
                new TranslatableText("container.enderchest")
            )
        );

        playerEntity.incrementStat(Stats.OPEN_ENDERCHEST);
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("open_gui"),
            new SerializableData()
                .add("gui_type", SerializableDataType.enumValue(GuiType.class), GuiType.POWER)
                .add("power", ApoliDataTypes.POWER_TYPE, null),
            OpenGuiAction::action
        );
    }
}
