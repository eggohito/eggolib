package io.github.eggohito.eggolib.condition.damage;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.access.DamageVictim;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

import java.util.function.Predicate;

public class AttackerCondition {

	public static boolean condition(SerializableData.Instance data, Pair<DamageSource, Float> sourceAndAmount) {

		Entity attacker = sourceAndAmount.getLeft().getAttacker();
		Entity victim = ((DamageVictim) sourceAndAmount.getLeft()).eggolib$get();

		Predicate<Entity> entityCondition = data.get("entity_condition");
		Predicate<Pair<Entity, Entity>> biEntityCondition = data.get("bientity_condition");

		return (entityCondition == null || entityCondition.test(attacker))
			&& (biEntityCondition == null || biEntityCondition.test(new Pair<>(attacker, victim)));

	}

	public static ConditionFactory<Pair<DamageSource, Float>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("attacker"),
			new SerializableData()
				.add("entity_condition", ApoliDataTypes.ENTITY_CONDITION, null)
				.add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null),
			AttackerCondition::condition
		);
	}

}
