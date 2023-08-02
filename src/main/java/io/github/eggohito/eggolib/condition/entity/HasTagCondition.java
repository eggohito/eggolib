package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HasTagCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		Set<String> specifiedScoreboardTags = new HashSet<>();
		Set<String> scoreboardTags = entity.getCommandTags();

		data.ifPresent("tag", specifiedScoreboardTags::add);
		data.ifPresent("tags", specifiedScoreboardTags::addAll);

		return specifiedScoreboardTags.isEmpty() ? !scoreboardTags.isEmpty() :
		       Collections.disjoint(scoreboardTags, specifiedScoreboardTags);

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("has_tag"),
			new SerializableData()
				.add("tag", SerializableDataTypes.STRING, null)
				.add("tags", SerializableDataTypes.STRINGS, null),
			HasTagCondition::condition
		);
	}

}
