package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.util.Key;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ModifyKeySequenceAction {

    private enum Operation {
        APPEND,
        INSERT,
        PREPEND,
        REMOVE,
        SET
    }

    public static void action(SerializableData.Instance data, Entity entity) {

        PowerHolderComponent.KEY.maybeGet(entity).ifPresent(
            powerHolderComponent -> {

                PowerType<?> targetPowerType = data.get("power");
                Power targetPower = powerHolderComponent.getPower(targetPowerType);

                if (!(targetPower instanceof ActionOnKeySequencePower aoksp)) return;

                Operation operation = data.get("operation");
                List<Key> specifiedKeys = data.get("keys");
                List<Key> currentKeySequence = aoksp.getCurrentKeySequence();

                int index = getSpecifiedOrLastIndex(data.isPresent("index") ? data.getInt("index") : -1, currentKeySequence);

                switch (operation) {
                    case APPEND -> currentKeySequence.addAll(specifiedKeys);
                    case INSERT -> currentKeySequence.addAll(index, specifiedKeys);
                    case PREPEND -> currentKeySequence.addAll(0, specifiedKeys);
                    case REMOVE -> {
                        if (!specifiedKeys.isEmpty()) currentKeySequence.removeAll(specifiedKeys);
                        else currentKeySequence.remove(index);
                    }
                    case SET -> {
                        currentKeySequence.clear();
                        currentKeySequence.addAll(specifiedKeys);
                    }
                }

            }
        );

    }

    private static int getSpecifiedOrLastIndex(int i, List<?> list) {

        int j = list.size() - 1;

        if (i == -1) return j;
        else return MathHelper.clamp(i, 0, j);

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("modify_key_sequence"),
            new SerializableData()
                .add("power", ApoliDataTypes.POWER_TYPE)
                .add("operation", SerializableDataType.enumValue(Operation.class), Operation.APPEND)
                .add("keys", EggolibDataTypes.BACKWARDS_COMPATIBLE_KEYS, new ArrayList<>())
                .add("index", SerializableDataTypes.INT, null),
            ModifyKeySequenceAction::action
        );
    }

}
