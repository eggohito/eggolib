package io.github.eggohito.apoli.condition.meta;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;

import java.util.List;

public class AndCondition {

    public static <T> boolean condition(SerializableData.Instance data, T t) {
        List<ConditionFactory<T>.Instance> conditions = data.get("conditions");
        return conditions.stream().allMatch(condition -> condition.test(t));
    }

    public static <T> ConditionFactory<T> getFactory(SerializableDataType<ConditionFactory<T>.Instance> dataType) {
        return new ConditionFactory<>(
            Apoli.identifier("and"),
            new SerializableData()
                .add("conditions", SerializableDataType.list(dataType)),
            AndCondition::condition
        );
    }

}
