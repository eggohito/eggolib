package io.github.eggohito.eggolib.action.meta;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.ScoreboardUtil;

import java.util.function.Consumer;

public class LoopAction {

    public static <T> void action(SerializableData.Instance data, T t) {

        int maxIterations = 0;

        Consumer<T> beforeAction = data.get("before_action");
        Consumer<T> afterAction = data.get("after_action");
        Consumer<T> action = data.get("action");

        if (data.isPresent("score")) maxIterations = ScoreboardUtil.getScore(data.get("score"));
        else if (data.isPresent("value")) maxIterations = data.getInt("value");

        if (beforeAction != null) beforeAction.accept(t);
        for (int i = 0; i < maxIterations; i++) {
            action.accept(t);
        }
        if (afterAction != null) afterAction.accept(t);

    }

    public static <T> ActionFactory<T> getFactory(SerializableDataType<ActionFactory<T>.Instance> actionDataType) {
        return new ActionFactory<>(
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
