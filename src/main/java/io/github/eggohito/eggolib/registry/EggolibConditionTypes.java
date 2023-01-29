package io.github.eggohito.eggolib.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class EggolibConditionTypes {

    public static final ConditionType<RegistryEntry<DimensionType>> DIMENSION_TYPE = new ConditionType<>("DimensionTypeCondition", EggolibRegistries.DIMENSION_TYPE_CONDITION);

}
