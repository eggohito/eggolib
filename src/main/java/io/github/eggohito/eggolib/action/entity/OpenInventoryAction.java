package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.InventoryPowerAccessor;
import io.github.eggohito.eggolib.power.EggolibInventoryPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.TranslatableText;

public class OpenInventoryAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return;
        if (!data.isPresent("power")) return; // TODO: Implement opening the inventory of the player if the `power` field is not present

        PowerType<?> targetPowerType = data.get("power");
        if (targetPowerType == null) return;

        Power targetPower = PowerHolderComponent.KEY.get(playerEntity).getPower(targetPowerType);

        ScreenHandlerFactory containerScreen = null;
        TranslatableText containerTitle = null;

        boolean valid = false;

        if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
            containerScreen = eggolibInventoryPower.getContainerScreen();
            containerTitle = eggolibInventoryPower.getContainerTitle();
            valid = true;
        }

        else if (targetPower instanceof InventoryPower apoliInventoryPower) {
            containerScreen = ((InventoryPowerAccessor) apoliInventoryPower).getFactory();
            containerTitle = ((InventoryPowerAccessor) apoliInventoryPower).getContainerName();
            valid = true;
        }

        if (valid) playerEntity.openHandledScreen(new SimpleNamedScreenHandlerFactory(containerScreen, containerTitle));

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
