package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;

public class CrawlingPower extends PosePower {

	public CrawlingPower(PowerType<?> powerType, LivingEntity livingEntity, int priority) {
		super(powerType, livingEntity, EntityPose.SWIMMING, priority);
	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("crawling"),
			new SerializableData()
				.add("priority", SerializableDataTypes.INT, 0),
			data -> (powerType, livingEntity) -> new CrawlingPower(
				powerType,
				livingEntity,
				data.get("priority")
			)
		).allowCondition();
	}

}
