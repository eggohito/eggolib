package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.MoonPhase;
import net.minecraft.entity.Entity;

import java.util.LinkedList;
import java.util.List;

public class MoonPhaseCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		List<MoonPhase> moonPhases = new LinkedList<>();

		data.<MoonPhase>ifPresent("phase", moonPhases::add);
		data.<List<MoonPhase>>ifPresent("phases", moonPhases::addAll);

		return moonPhases.stream().anyMatch(moonPhase -> moonPhase.matches(entity.world.getMoonPhase()));

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("moon_phase"),
			new SerializableData()
				.add("phase", EggolibDataTypes.MOON_PHASE, null)
				.add("phases", EggolibDataTypes.MOON_PHASES, null),
			MoonPhaseCondition::condition
		);
	}

}
