package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibComponents;
import net.minecraft.entity.Entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HasTagCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		Set<String> specifiedCommandTags = new HashSet<>();
		Set<String> commandTags = EggolibComponents.MISC.get(entity).getCommandTags();

		data.ifPresent("tag", specifiedCommandTags::add);
		data.ifPresent("tags", specifiedCommandTags::addAll);

		return specifiedCommandTags.isEmpty()
			? !commandTags.isEmpty()
			: !Collections.disjoint(commandTags, specifiedCommandTags);

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
