package io.github.eggohito.eggolib.condition.bientity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

public class EqualCondition {

	public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {
		return actorAndTarget.getLeft().equals(actorAndTarget.getRight());
	}

	public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("equal"),
			new SerializableData(),
			EqualCondition::condition
		);
	}

}
