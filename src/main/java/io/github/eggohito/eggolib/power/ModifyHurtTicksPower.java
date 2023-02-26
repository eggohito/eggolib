package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModifyHurtTicksPower extends Power {

    private final Consumer<Pair<Entity, Entity>> biEntityAction;

    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final Predicate<Pair<DamageSource, Float>> damageCondition;

    private final List<Modifier> modifiers = new LinkedList<>();
    private final List<Modifier> immunityModifiers = new LinkedList<>();

    public ModifyHurtTicksPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<Pair<Entity, Entity>> biEntityCondition, Predicate<Pair<DamageSource, Float>> damageCondition, Modifier modifier, List<Modifier> modifiers, Modifier immunityModifier, List<Modifier> immunityModifiers) {

        super(powerType, livingEntity);

        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.damageCondition = damageCondition;

        if (modifier != null) this.modifiers.add(modifier);
        if (modifiers != null) this.modifiers.addAll(modifiers);

        if (immunityModifier != null) this.immunityModifiers.add(immunityModifier);
        if (immunityModifiers != null) this.immunityModifiers.addAll(immunityModifiers);

    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public List<Modifier> getImmunityModifiers() {
        return immunityModifiers;
    }

    public boolean doesApply(DamageSource damageSource, float damageAmount, Entity attacker) {
        return (damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount))) && (biEntityCondition == null || biEntityCondition.test(new Pair<>(attacker, entity)));
    }

    public void executeActions(Entity attacker) {
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
                .add("modifiers", Modifier.LIST_TYPE, null)
                .add("immunity_modifier", Modifier.DATA_TYPE, null)
                .add("immunity_modifiers", Modifier.LIST_TYPE, null),
            data -> (powerType, livingEntity) -> new ModifyHurtTicksPower(
                powerType,
                livingEntity,
                data.get("bientity_action"),
                data.get("bientity_condition"),
                data.get("damage_condition"),
                data.get("modifier"),
                data.get("modifiers"),
                data.get("immunity_modifier"),
                data.get("immunity_modifiers")
            )
        ).allowCondition();
    }

}
