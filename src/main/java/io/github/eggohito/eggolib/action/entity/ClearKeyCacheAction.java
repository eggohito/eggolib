package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import net.minecraft.entity.Entity;

public class ClearKeyCacheAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        PowerHolderComponent.KEY.maybeGet(entity).ifPresent(
            powerHolderComponent -> {

                PowerType<?> powerType = data.get("power");
                Power power = powerHolderComponent.getPower(powerType);

                if (!(power instanceof ActionOnKeySequencePower aoksp)) return;

                aoksp.getCurrentKeySequence().clear();
                PowerHolderComponent.syncPower(entity, aoksp.getType());

            }
        );

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("clear_key_cache"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE),
            ClearKeyCacheAction::action
        );
    }

}
