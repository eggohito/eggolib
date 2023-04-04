package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.entity.*;
import io.github.eggohito.eggolib.condition.meta.ChanceCondition;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EggolibEntityConditions {

    public static void register() {
        register(BreakingBlockCondition.getFactory());
        register(ChanceCondition.getFactory());
        register(CrawlingCondition.getFactory());
        register(DimensionCondition.getFactory());
        register(EggolibBlockCollisionCondition.getFactory());
        register(EggolibInBlockCondition.getFactory());
        register(HasTagCondition.getFactory());
        register(InScreenCondition.getFactory());
        register(InTeamCondition.getFactory());
        register(InventoryCondition.getFactory());
        register(MoonPhaseCondition.getFactory());
        register(PermissionLevelCondition.getFactory());
        register(PerspectiveCondition.getFactory());
        register(ScoreboardCondition.getFactory());
    }

    public static ConditionFactory<Entity> register(ConditionFactory<Entity> conditionFactory) {
        return Registry.register(ApoliRegistries.ENTITY_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
