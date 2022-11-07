package io.github.eggohito.eggolib.condition.dimension_type;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public class FixedTimeCondition {

    public static boolean condition(SerializableData.Instance data, RegistryEntry<DimensionType> dimensionTypeRegistryEntry) {

        OptionalLong fixedTime = dimensionTypeRegistryEntry.value().fixedTime();
        if (!(data.isPresent("comparison") || data.isPresent("compare_to"))) return fixedTime.isPresent();

        Comparison comparison = data.get("comparison");
        int specifiedValue = data.get("compare_to");

        return fixedTime.isPresent() && comparison.compare(fixedTime.getAsLong(), specifiedValue);

    }

    public static ConditionFactory<RegistryEntry<DimensionType>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("fixed_time"),
            new SerializableData()
                .add("comparison", ApoliDataTypes.COMPARISON, null)
                .add("compare_to", SerializableDataTypes.INT, null),
            FixedTimeCondition::condition
        );
    }

}
