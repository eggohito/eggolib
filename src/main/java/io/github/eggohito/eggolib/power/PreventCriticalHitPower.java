package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class PreventCriticalHitPower extends PrioritizedPower {

    private final Consumer<Pair<Entity, Entity>> biEntityAction;
    private final Predicate<Pair<Entity, Entity>> bientityCondition;
    private final Predicate<Pair<DamageSource, Float>> damageCondition;

    public PreventCriticalHitPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<Pair<Entity, Entity>> bientityCondition, Predicate<Pair<DamageSource, Float>> damageCondition, int priority) {
        super(powerType, livingEntity, priority);
        this.biEntityAction = biEntityAction;
        this.bientityCondition = bientityCondition;
        this.damageCondition = damageCondition;
    }

    public boolean doesApply(Entity target, DamageSource damageSource, float damageAmount) {
        return (damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount))) &&
               (bientityCondition == null || bientityCondition.test(new Pair<>(entity, target)));
    }

    public void executeActions(Entity target) {
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(entity, target));
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("prevent_critical_hit"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("damage_condition", SerializableDataTypes.DAMAGE_SOURCE, null)
                .add("priority", SerializableDataTypes.INT, 0),
            data -> (powerType, livingEntity) -> new PreventCriticalHitPower(
                powerType,
                livingEntity,
                data.get("bientity_action"),
                data.get("bientity_condition"),
                data.get("damage_condition"),
                data.get("priority")
            )
        ).allowCondition();
    }

}
