package io.github.eggohito.eggolib.condition.bientity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.Tameable;
import net.minecraft.util.Pair;

public class OwnerCondition {

	public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

		Entity actor = actorAndTarget.getLeft();
		Entity target = actorAndTarget.getRight();

		return (target instanceof Tameable tameableEntity && actor == tameableEntity.getOwner())
			|| (target instanceof Ownable ownableEntity && actor == ownableEntity.getOwner());

	}

	public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("owner"),
			new SerializableData(),
			OwnerCondition::condition
		);
	}

}
