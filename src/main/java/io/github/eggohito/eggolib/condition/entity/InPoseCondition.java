package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.entity.Entity;

public class InPoseCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {
		return entity.isInPose(data.get("pose"));
	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("in_pose"),
			new SerializableData()
				.add("pose", EggolibDataTypes.ENTITY_POSE),
			InPoseCondition::condition
		);
	}

}
