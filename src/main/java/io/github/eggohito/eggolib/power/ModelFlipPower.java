package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.LivingEntity;

public class ModelFlipPower extends Power {

	public ModelFlipPower(PowerType<?> powerType, LivingEntity livingEntity) {
		super(powerType, livingEntity);
	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("model_flip"),
			new SerializableData(),
			data -> ModelFlipPower::new
		).allowCondition();
	}

}
