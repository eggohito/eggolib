package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.Prioritized;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;

public class PosePower extends Power implements Prioritized<PosePower> {

	private final EntityPose pose;
	private final int priority;

	public PosePower(PowerType<?> type, LivingEntity entity, EntityPose pose, int priority) {
		super(type, entity);
		this.pose = pose;
		this.priority = priority;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	public EntityPose getPose() {
		return pose;
	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("pose"),
			new SerializableData()
				.add("pose", EggolibDataTypes.ENTITY_POSE)
				.add("priority", SerializableDataTypes.INT, 0),
			data -> (powerType, entity) -> new PosePower(
				powerType,
				entity,
				data.get("pose"),
				data.get("priority")
			)
		).allowCondition();
	}

}
