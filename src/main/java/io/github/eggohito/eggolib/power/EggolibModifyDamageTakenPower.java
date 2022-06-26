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

public class EggolibModifyDamageTakenPower extends ValueModifyingPower {

    private final Consumer<Entity> selfAction;
    private final Consumer<Entity> attackerAction;
    private final Consumer<Pair<Entity, Entity>> biEntityAction;

    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final Predicate<Pair<DamageSource, Float>> damageCondition;

    public EggolibModifyDamageTakenPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Entity> selfAction, Consumer<Entity> attackerAction, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<Pair<Entity, Entity>> biEntityCondition, Predicate<Pair<DamageSource, Float>> damageCondition) {
        super(powerType, livingEntity);
        this.selfAction = selfAction;
        this.attackerAction = attackerAction;
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.damageCondition = damageCondition;
    }

    public boolean doesApply(DamageSource damageSource, float damageAmount, Entity attacker) {
        return (attacker == null || (biEntityCondition == null || biEntityCondition.test(new Pair<>(damageSource.getAttacker(), entity)))) &&
               (damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount)));
    }

    public void executeActions(Entity attacker) {
        if (selfAction != null) selfAction.accept(entity);
        if (attackerAction != null) attackerAction.accept(attacker);
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(attacker, entity));
    }

    public static PowerFactory<?> getFactory() {

        return new PowerFactory<>(
            Eggolib.identifier("modify_damage_taken"),
            new SerializableData()
                .add("self_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("attacker_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER,null)
                .add("modifiers", SerializableDataTypes.ATTRIBUTE_MODIFIERS, null),
            data -> (powerType, livingEntity) -> {

                EggolibModifyDamageTakenPower emdtp = new EggolibModifyDamageTakenPower(
                    powerType,
                    livingEntity,
                    data.get("self_action"),
                    data.get("attacker_action"),
                    data.get("bientity_action"),
                    data.get("bientity_condition"),
                    data.get("damage_condition")
                );

                if (data.isPresent("modifier")) emdtp.addModifier(data.getModifier("modifier"));
                if (data.isPresent("modifiers")) {
                    List<EntityAttributeModifier> modifiers = data.get("modifiers");
                    modifiers.forEach(emdtp::addModifier);
                }

                return emdtp;

            }
        ).allowCondition();

    }

}
