package io.github.eggohito.eggolib.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.dimension.DimensionType;

public class EggolibRegistries {

    public static final Registry<ConditionFactory<DimensionType>> DIMENSION_CONDITION;

    static {
        DIMENSION_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<DimensionType>>castClass(ConditionFactory.class), Eggolib.identifier("dimension_condition")).buildAndRegister();
    }

}
