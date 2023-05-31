package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.content.EggolibDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class ChangeHealthAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		ResourceOperation operation = data.get("operation");
		float newHealthValue = data.getFloat("change");
		float oldHealthValue = livingEntity.getHealth();
		float result = oldHealthValue + newHealthValue;
		float value = operation == ResourceOperation.SET ? newHealthValue : result;

		if (result <= 0F) {
			livingEntity.damage(entity.getDamageSources().create(EggolibDamageTypes.CHANGE_HEALTH_UNDERFLOW), livingEntity.getMaxHealth());
		} else {
			livingEntity.setHealth(MathHelper.clamp(value, 1F, livingEntity.getMaxHealth()));
		}

	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("change_health"),
			new SerializableData()
				.add("change", SerializableDataTypes.FLOAT)
				.add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD),
			ChangeHealthAction::action
		);
	}

}
