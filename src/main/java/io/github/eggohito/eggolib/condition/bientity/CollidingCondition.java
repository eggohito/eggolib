package io.github.eggohito.eggolib.condition.bientity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class CollidingCondition {

	public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

		Box actorBoundingBox = actorAndTarget.getLeft().getBoundingBox();
		Box targetBoundingBox = actorAndTarget.getRight().getBoundingBox();
		Vec3d offset = data.get("offset");

		Box offsetActorBoundingBox = actorBoundingBox;
		if (offset != null) {
			offsetActorBoundingBox = actorBoundingBox.offset(
				offset.getX() * actorBoundingBox.getLengthX(),
				offset.getY() * actorBoundingBox.getLengthY(),
				offset.getZ() * actorBoundingBox.getLengthZ()
			);
		}

		return offsetActorBoundingBox.intersects(targetBoundingBox);

	}

	public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("colliding"),
			new SerializableData()
				.add("offset", SerializableDataTypes.VECTOR, null),
			CollidingCondition::condition
		);
	}

}
