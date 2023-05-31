package io.github.eggohito.eggolib.condition.bientity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibComponents;
import io.github.eggohito.eggolib.component.entity.IMiscComponent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class HasMatchingTagCondition {

	public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

		Optional<IMiscComponent> actorMiscComponent = EggolibComponents.MISC.maybeGet(actorAndTarget.getLeft());
		Optional<IMiscComponent> targetMiscComponent = EggolibComponents.MISC.maybeGet(actorAndTarget.getRight());

		if (actorMiscComponent.isEmpty() || targetMiscComponent.isEmpty()) {
			return false;
		}

		Set<String> actorScoreboardTags = actorMiscComponent.get().getScoreboardTags();
		Set<String> targetScoreboardTags = targetMiscComponent.get().getScoreboardTags();

		return !Collections.disjoint(actorScoreboardTags, targetScoreboardTags);

	}

	public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("has_matching_tag"),
			new SerializableData(),
			HasMatchingTagCondition::condition
		);
	}

}
