package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.List;

public class RemovePowerAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        PowerHolderComponent.KEY.maybeGet(entity).ifPresent(
            powerHolderComponent -> {

                PowerType<?> targetPowerType = data.get("power");
                if (targetPowerType == null) return;

                List<Identifier> targetPowerSources = powerHolderComponent.getSources(targetPowerType);
                for (Identifier targetPowerSource : targetPowerSources) {
                    powerHolderComponent.removePower(targetPowerType, targetPowerSource);
                }

                if (targetPowerSources.size() > 0) {
                    powerHolderComponent.sync();
                }

            }
        );

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("remove_power"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE),
            RemovePowerAction::action
        );
    }

}
