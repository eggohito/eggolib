package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.eggohito.eggolib.registry.EggolibRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class EggolibDimensionTypeConditions {

    public static void register() {

    }

    private static void register(ConditionFactory<RegistryEntry<DimensionType>> conditionFactory) {
        Registry.register(EggolibRegistries.DIMENSION_TYPE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
