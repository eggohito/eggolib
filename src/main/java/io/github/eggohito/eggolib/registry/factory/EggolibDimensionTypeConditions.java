package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.eggohito.apoli.condition.meta.AndCondition;
import io.github.eggohito.apoli.condition.meta.ConstantCondition;
import io.github.eggohito.apoli.condition.meta.OrCondition;
import io.github.eggohito.eggolib.condition.dimension_type.*;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.registry.EggolibRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;

public class EggolibDimensionTypeConditions {

    public static void register() {

        //  Register Apoli's meta condition types
        register(AndCondition.getFactory(EggolibDataTypes.DIMENSION_TYPE_CONDITION));
        register(ConstantCondition.getFactory(EggolibDataTypes.DIMENSION_TYPE_CONDITION));
        register(OrCondition.getFactory(EggolibDataTypes.DIMENSION_TYPE_CONDITION));

        //  Register eggolib's dimension type condition types
        register(AmbientLightCondition.getFactory());
        register(BedWorksCondition.getFactory());
        register(CoordinateScaleCondition.getFactory());
        register(EffectsCondition.getFactory());
        register(FixedTimeCondition.getFactory());
        register(HasCeilingCondition.getFactory());
        register(HasRaidsCondition.getFactory());
        register(HasSkylightCondition.getFactory());
        register(HeightCondition.getFactory());
        register(InfiniburnCondition.getFactory());
        register(IsNaturalCondition.getFactory());
        register(IsPiglinSafeCondition.getFactory());
        register(IsUltrawarmCondition.getFactory());
        register(LogicalHeightCondition.getFactory());
        register(MinYCondition.getFactory());
        register(MonsterSpawnBlockLightLimitCondition.getFactory());
        register(MonsterSpawnLightLevelCondition.getFactory());
        register(RespawnAnchorWorksCondition.getFactory());

    }

    private static void register(ConditionFactory<RegistryEntry<DimensionType>> conditionFactory) {
        Registry.register(EggolibRegistries.DIMENSION_TYPE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
