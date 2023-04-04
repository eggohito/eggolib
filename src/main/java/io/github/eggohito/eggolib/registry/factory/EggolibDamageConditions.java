package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.damage.EggolibProjectileCondition;
import io.github.eggohito.eggolib.condition.damage.NbtCondition;
import io.github.eggohito.eggolib.condition.meta.ChanceCondition;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class EggolibDamageConditions {

    public static void register() {
        register(ChanceCondition.getFactory());
        register(EggolibProjectileCondition.getFactory());
        register(NbtCondition.getFactory());
    }

    public static ConditionFactory<Pair<DamageSource, Float>> register(ConditionFactory<Pair<DamageSource, Float>> conditionFactory) {
        return Registry.register(ApoliRegistries.DAMAGE_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
