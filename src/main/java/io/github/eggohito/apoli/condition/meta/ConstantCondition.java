package io.github.eggohito.apoli.condition.meta;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;

public class ConstantCondition {

	public static <T> boolean condition(SerializableData.Instance data, T t) {
		return data.getBoolean("value");
	}

	public static <T> ConditionFactory<T> getFactory(SerializableDataType<ConditionFactory<T>.Instance> dataType) {
		return new ConditionFactory<>(
			Apoli.identifier("constant"),
			new SerializableData()
				.add("value", SerializableDataTypes.BOOLEAN),
			ConstantCondition::condition
		);
	}

}
