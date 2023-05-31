package io.github.eggohito.eggolib.condition.meta;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;

import java.util.Random;

public class ChanceCondition {

	public static <T> boolean condition(SerializableData.Instance data, T t) {
		return new Random().nextFloat() < data.getFloat("chance");
	}

	public static <T> ConditionFactory<T> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("chance"),
			new SerializableData()
				.add("chance", SerializableDataTypes.FLOAT),
			ChanceCondition::condition
		);
	}

}
