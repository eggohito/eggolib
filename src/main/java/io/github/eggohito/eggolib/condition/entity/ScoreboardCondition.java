package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.ScoreboardUtil;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Collections;
import java.util.Optional;

public class ScoreboardCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		ScoreHolderArgumentType.ScoreHolder name;
		String objective = data.get("objective");
		Comparison comparison = data.get("comparison");
		int compareTo = data.get("compare_to");

		if (data.isPresent("name")) {
			name = data.<ArgumentWrapper<ScoreHolderArgumentType.ScoreHolder>>get("name").get();
		} else {
			name = (source, players) -> Collections.singleton(entity instanceof PlayerEntity playerEntity ? playerEntity.getEntityName() : entity.getUuidAsString());
		}

		Optional<Integer> score = ScoreboardUtil.getScore(entity, name, objective);
		return score.isPresent() && comparison.compare(score.get(), compareTo);

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("scoreboard"),
			new SerializableData()
				.add("name", EggolibDataTypes.SCORE_HOLDER, null)
				.add("objective", SerializableDataTypes.STRING)
				.add("comparison", ApoliDataTypes.COMPARISON)
				.add("compare_to", SerializableDataTypes.INT),
			ScoreboardCondition::condition
		);
	}

}
