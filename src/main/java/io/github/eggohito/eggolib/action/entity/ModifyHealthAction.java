package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.content.EggolibDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ModifyHealthAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		if (!(entity instanceof LivingEntity livingEntity)) {
			return;
		}

		Modifier modifier = data.get("modifier");
		float maxHealth = livingEntity.getMaxHealth();
		float newHealth = (float) modifier.apply(livingEntity, maxHealth);

		if (newHealth > maxHealth) {
			newHealth -= maxHealth;
		}

		if (newHealth <= 0F) {
			livingEntity.damage(entity.getDamageSources().create(EggolibDamageTypes.CHANGE_HEALTH_UNDERFLOW), maxHealth);
		} else {
			livingEntity.setHealth(newHealth);
		}

	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("modify_health"),
			new SerializableData()
				.add("modifier", Modifier.DATA_TYPE),
			ModifyHealthAction::action
		);
	}

}
