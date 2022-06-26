package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EggolibModifyDamageDealtPower extends ValueModifyingPower {

    private final Consumer<Entity> selfAction;
    private final Consumer<Entity> targetAction;
    private final Consumer<Pair<Entity, Entity>> biEntityAction;

    private final Predicate<Entity> targetCondition;
    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final Predicate<Pair<DamageSource, Float>> damageCondition;

    public EggolibModifyDamageDealtPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Entity> selfAction, Consumer<Entity> targetAction, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<Entity> targetCondition, Predicate<Pair<Entity, Entity>> biEntityCondition, Predicate<Pair<DamageSource, Float>> damageCondition) {
        super(powerType, livingEntity);
        this.selfAction = selfAction;
        this.targetAction = targetAction;
        this.biEntityAction = biEntityAction;
        this.targetCondition = targetCondition;
        this.biEntityCondition = biEntityCondition;
        this.damageCondition = damageCondition;
    }

    public boolean doesApply(DamageSource damageSource, float damageAmount, Entity target) {
        return (target == null || (biEntityCondition == null || biEntityCondition.test(new Pair<>(entity, target)))) &&
               (damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount))) &&
               (target == null || (targetCondition == null || targetCondition.test(target)));
    }

    public void executeActions(Entity target) {
        if (selfAction != null) selfAction.accept(entity);
        if (targetAction != null) targetAction.accept(target);
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(entity, target));
    }

    public static PowerFactory<?> getFactory() {

        return new PowerFactory<>(
            Eggolib.identifier("modify_damage_dealt"),
            new SerializableData()
                .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("target_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("target_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER, null)
                .add("modifiers", SerializableDataTypes.ATTRIBUTE_MODIFIERS, null),
            data -> (powerType, livingEntity) -> {

                EggolibModifyDamageDealtPower emddp = new EggolibModifyDamageDealtPower(
                    powerType,
                    livingEntity,
                    data.get("self_action"),
                    data.get("target_action"),
                    data.get("bientity_action"),
                    data.get("target_condition"),
                    data.get("bientity_condition"),
                    data.get("damage_condition")
                );

                if (data.isPresent("modifier")) emddp.addModifier(data.getModifier("modifier"));
                if (data.isPresent("modifiers")) {
                    List<EntityAttributeModifier> modifiers = data.get("modifiers");
                    modifiers.forEach(emddp::addModifier);
                }

                return emddp;

            }
        ).allowCondition();

    }

}
