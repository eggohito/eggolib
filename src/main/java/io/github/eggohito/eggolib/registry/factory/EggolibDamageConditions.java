package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.damage.EggolibProjectileCondition;
import io.github.eggohito.eggolib.condition.meta.ChanceCondition;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EggolibDamageConditions {

    public static void register() {
        register(EggolibProjectileCondition.getFactory());
        register(ChanceCondition.getFactory());
    }

    public static void register(ConditionFactory<Pair<DamageSource, Float>> conditionFactory) {
        Registry.register(ApoliRegistries.DAMAGE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
