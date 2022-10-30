package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DimensionCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        Predicate<RegistryEntry<DimensionType>> dimensionTypeCondition = data.get("dimension_condition");
        List<Identifier> worldIds = new ArrayList<>();
        int i = 0;

        data.<Identifier>ifPresent("dimension", worldIds::add);
        data.<List<Identifier>>ifPresent("dimensions", worldIds::addAll);

        i += worldIds.stream().anyMatch(worldId -> entity.world.getRegistryKey() == RegistryKey.of(Registry.WORLD_KEY, worldId)) ? 1 : 0;
        i += dimensionTypeCondition == null || dimensionTypeCondition.test(entity.world.getDimensionEntry()) ? 1 : 0;

        return i > 0;

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("dimension"),
            new SerializableData()
                .add("dimension", SerializableDataTypes.IDENTIFIER, null)
                .add("dimensions", SerializableDataTypes.IDENTIFIERS, null)
                .add("dimension_type_condition", EggolibDataTypes.DIMENSION_TYPE_CONDITION, null),
            DimensionCondition::condition
        );
    }

}
