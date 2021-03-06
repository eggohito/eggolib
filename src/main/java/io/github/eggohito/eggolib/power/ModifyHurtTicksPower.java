package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModifyHurtTicksPower extends ValueModifyingPower {

    private final Consumer<Pair<Entity, Entity>> biEntityAction;

    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final Predicate<Pair<DamageSource, Float>> damageCondition;

    public ModifyHurtTicksPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<Pair<Entity, Entity>> biEntityCondition, Predicate<Pair<DamageSource, Float>> damageCondition) {
        super(powerType, livingEntity);
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.damageCondition = damageCondition;
    }

    public boolean doesApply(DamageSource damageSource, float damageAmount, Entity attacker) {
        return (damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount))) && (biEntityCondition == null || biEntityCondition.test(new Pair<>(attacker, entity)));
    }

    public void apply(Entity attacker) {
        entity.timeUntilRegen = MathHelper.clamp((int) PowerHolderComponent.modify(entity, this.getClass(), entity.timeUntilRegen), 0, Integer.MAX_VALUE);
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(attacker, entity));
    }

    public static PowerFactory<?> getFactory() {

        return new PowerFactory<>(
            Eggolib.identifier("modify_hurt_ticks"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null),
            data -> (powerType, livingEntity) -> {

                ModifyHurtTicksPower mhtp = new ModifyHurtTicksPower(
                    powerType,
                    livingEntity,
                    data.get("bientity_action"),
                    data.get("bientity_condition"),
                    data.get("damage_condition")
                );

                data.ifPresent("modifier", mhtp::addModifier);
                data.<List<Modifier>>ifPresent("modifiers", modifiers -> modifiers.forEach(mhtp::addModifier));

                return mhtp;

            }
        ).allowCondition();

    }

}
