package io.github.eggohito.eggolib.registry;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class EggolibRegistries {

    public static final Registry<ConditionFactory<RegistryEntry<DimensionType>>> DIMENSION_TYPE_CONDITION;

    static {
        DIMENSION_TYPE_CONDITION = FabricRegistryBuilder.createSimple(ClassUtil.<ConditionFactory<RegistryEntry<DimensionType>>>castClass(ConditionFactory.class), Eggolib.identifier("dimension_type_condition")).buildAndRegister();
    }

}
