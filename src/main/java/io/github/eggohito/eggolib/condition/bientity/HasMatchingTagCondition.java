package io.github.eggohito.eggolib.condition.bientity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibComponents;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.Set;

public class HasMatchingTagCondition {

	public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

		Set<String> actorCommandTags = EggolibComponents.MISC.get(actorAndTarget.getLeft()).getCommandTags();
		Set<String> targetCommandTags = EggolibComponents.MISC.get(actorAndTarget.getRight()).getCommandTags();

		return !Collections.disjoint(actorCommandTags, targetCommandTags);

	}

	public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("has_matching_tag"),
			new SerializableData(),
			HasMatchingTagCondition::condition
		);
	}

}
