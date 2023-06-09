package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.access.WeatherView;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class InSnowCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		BlockPos downBlockPos = entity.getBlockPos();
		BlockPos upBlockPos = BlockPos.ofFloored(downBlockPos.getX(), entity.getBoundingBox().maxY, downBlockPos.getZ());

		return ((WeatherView) entity.world).hasSnow(downBlockPos, upBlockPos);

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("in_snow"),
			new SerializableData(),
			InSnowCondition::condition
		);
	}

}
