package io.github.eggohito.eggolib.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionType;
import net.minecraft.world.dimension.DimensionType;

public class EggolibConditionTypes {

    public static final ConditionType<DimensionType> DIMENSION = new ConditionType<>("DimensionCondition", EggolibRegistries.DIMENSION_CONDITION);

}
