package io.github.eggohito.eggolib.condition.block;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;

public class AirCondition {

	public static boolean condition(SerializableData.Instance data, CachedBlockPosition block) {
		return block.getBlockState().isAir();
	}

	public static ConditionFactory<CachedBlockPosition> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("air"),
			new SerializableData(),
			AirCondition::condition
		);
	}
}
