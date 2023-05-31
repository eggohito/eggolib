package io.github.eggohito.eggolib.action.bientity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.MiscUtil;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class DamageAction {

	public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

		Float damageAmount = data.get("amount");
		List<Modifier> modifiers = new LinkedList<>();

		data.<Modifier>ifPresent("modifier", modifiers::add);
		data.<List<Modifier>>ifPresent("modifiers", modifiers::addAll);

		if (!modifiers.isEmpty() && actorAndTarget.getRight() instanceof LivingEntity livingTargetEntity) {

			float targetMaxHealth = livingTargetEntity.getMaxHealth();
			float newDamageAmount = (float) ModifierUtil.applyModifiers(livingTargetEntity, modifiers, targetMaxHealth);

			if (newDamageAmount > targetMaxHealth) {
				damageAmount = newDamageAmount - targetMaxHealth;
			} else {
				damageAmount = newDamageAmount;
			}

		}

		DamageSource damageSource = MiscUtil.createDamageSource(
			actorAndTarget.getLeft().getDamageSources(),
			data.get("source"),
			data.get("damage_type"),
			actorAndTarget.getLeft()
		);

		if (damageAmount != null) {
			actorAndTarget.getRight().damage(damageSource, damageAmount);
		}

	}

	public static ActionFactory<Pair<Entity, Entity>> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("damage"),
			new SerializableData()
				.add("amount", SerializableDataTypes.FLOAT, null)
				.add("source", ApoliDataTypes.DAMAGE_SOURCE_DESCRIPTION, null)
				.add("damage_type", SerializableDataTypes.DAMAGE_TYPE, null)
				.add("modifier", Modifier.DATA_TYPE, null)
				.add("modifiers", Modifier.LIST_TYPE, null),
			DamageAction::action
		);
	}

}
