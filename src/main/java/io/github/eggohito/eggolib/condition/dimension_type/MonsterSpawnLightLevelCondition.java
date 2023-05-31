package io.github.eggohito.eggolib.condition.dimension_type;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

public class MonsterSpawnLightLevelCondition {

	public static boolean condition(SerializableData.Instance data, RegistryEntry<DimensionType> dimensionTypeRegistryEntry) {

		IntProvider monsterSpawnLightLevelProvider = dimensionTypeRegistryEntry.value().monsterSpawnLightTest();
		List<Integer> monsterSpawnLightLevelList = new ArrayList<>();
		Comparison comparison = data.get("comparison");
		int specifiedValue = data.getInt("compare_to");

		for (int i = monsterSpawnLightLevelProvider.getMin(); i <= monsterSpawnLightLevelProvider.getMax(); i++) {
			monsterSpawnLightLevelList.add(i);
		}

		return monsterSpawnLightLevelList.stream().anyMatch(lightLevel -> comparison.compare(lightLevel, specifiedValue));

	}

	public static ConditionFactory<RegistryEntry<DimensionType>> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("monster_spawn_light_level"),
			new SerializableData()
				.add("comparison", ApoliDataTypes.COMPARISON)
				.add("compare_to", SerializableDataTypes.INT),
			MonsterSpawnLightLevelCondition::condition
		);
	}

}
