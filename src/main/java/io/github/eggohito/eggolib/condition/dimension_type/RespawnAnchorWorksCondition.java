package io.github.eggohito.eggolib.condition.dimension_type;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class RespawnAnchorWorksCondition {

    public static boolean condition(SerializableData.Instance data, RegistryEntry<DimensionType> dimensionTypeRegistryEntry) {
        return dimensionTypeRegistryEntry.value().respawnAnchorWorks();
    }

    public static ConditionFactory<RegistryEntry<DimensionType>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("respawn_anchor_works"),
            new SerializableData(),
            RespawnAnchorWorksCondition::condition
        );
    }

}
