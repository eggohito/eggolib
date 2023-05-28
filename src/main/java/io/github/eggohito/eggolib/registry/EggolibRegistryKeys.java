package io.github.eggohito.eggolib.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public interface EggolibRegistryKeys {

    RegistryKey<Registry<ConditionFactory<RegistryEntry<DimensionType>>>> DIMENSION_TYPE_CONDITION = RegistryKey.ofRegistry(Eggolib.identifier("dimension_type_condition"));

}
