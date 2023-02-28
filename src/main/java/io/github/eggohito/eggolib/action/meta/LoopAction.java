package io.github.eggohito.eggolib.action.meta;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.ScoreboardUtil;

public class LoopAction {

    public static <T> void action(SerializableData.Instance data, T t) {

        int iterations = 0;

        ActionFactory<T>.Instance beforeAction = data.get("before_action");
        ActionFactory<T>.Instance afterAction = data.get("after_action");
        ActionFactory<T>.Instance action = data.get("action");

        if (data.isPresent("score")) iterations = ScoreboardUtil.getScore(data.get("score"));
        else if (data.isPresent("value")) iterations = data.getInt("value");

        if (iterations <= 0) return;

        if (beforeAction != null) beforeAction.accept(t);
        for (int i = 0; i < iterations; i++) {
            action.accept(t);
        }
        if (afterAction != null) afterAction.accept(t);

    }

    public static <T> ActionFactory<T> getFactory(SerializableDataType<ActionFactory<T>.Instance> actionDataType) {

        return new ActionFactory<T>(
            Eggolib.identifier("loop"),
            new SerializableData()
                .add("before_action", actionDataType, null)
                .add("action", actionDataType, null)
                .add("after_action", actionDataType, null)
                .add("score", EggolibDataTypes.SCOREBOARD, null)
                .add("value", SerializableDataTypes.INT, null),
            LoopAction::action
        );

    }

}
