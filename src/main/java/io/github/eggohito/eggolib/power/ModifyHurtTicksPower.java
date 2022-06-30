package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModifyHurtTicksPower extends ValueModifyingPower {

    private int oldMaxHurtTime;
    private int oldTimeUntilRegen;
    private boolean wasActive = false;

    private final Consumer<Pair<Entity, Entity>> biEntityAction;

    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final Predicate<Pair<DamageSource, Float>> damageCondition;

    public ModifyHurtTicksPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<Pair<Entity, Entity>> biEntityCondition, Predicate<Pair<DamageSource, Float>> damageCondition) {
        super(powerType, livingEntity);
        this.biEntityAction = biEntityAction;
        this.biEntityCondition = biEntityCondition;
        this.damageCondition = damageCondition;
        this.setTicking(true);
    }

    @Override
    public void tick() {
        if (isActive() && !wasActive) wasActive = true;
        else if (wasActive) {
            entity.maxHurtTime = oldMaxHurtTime;
            wasActive = false;
        }
    }

    @Override
    public void onGained() {
        getHurtTicks();
    }

    @Override
    public void onLost() {
        entity.maxHurtTime = oldMaxHurtTime;
    }

    @Override
    public NbtElement toTag() {

        NbtCompound nbtCompound = new NbtCompound();

        nbtCompound.putInt("OldMaxHurtTime", oldMaxHurtTime);
        nbtCompound.putInt("OldTimeUntilRegen", oldTimeUntilRegen);

        return nbtCompound;

    }

    @Override
    public void fromTag(NbtElement tag) {
        if (tag instanceof NbtCompound nbtCompound) {
            oldMaxHurtTime = nbtCompound.getInt("OldMaxHurtTime");
            oldTimeUntilRegen = nbtCompound.getInt("OldTimeUntilRegen");
        }
    }

    public boolean doesApply(DamageSource damageSource, float damageAmount, Entity attacker) {
        return (damageCondition == null || damageCondition.test(new Pair<>(damageSource, damageAmount))) && (biEntityCondition == null || biEntityCondition.test(new Pair<>(attacker, entity)));
    }

    public void apply(Entity attacker) {
        getHurtTicks();
        entity.maxHurtTime = modify(oldMaxHurtTime);
        entity.timeUntilRegen = modify(oldTimeUntilRegen);
        entity.hurtTime = entity.maxHurtTime;
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(attacker, entity));
    }

    private void getHurtTicks() {
        oldMaxHurtTime = entity.maxHurtTime;
        oldTimeUntilRegen = entity.timeUntilRegen;
    }

    private int modify(int value) {
        return MathHelper.clamp((int) PowerHolderComponent.modify(entity, this.getClass(), value), 0, Integer.MAX_VALUE);
    }

    public static PowerFactory<?> getFactory() {

        return new PowerFactory<>(
            Eggolib.identifier("modify_hurt_ticks"),
            new SerializableData()
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("damage_condition", ApoliDataTypes.DAMAGE_CONDITION, null)
                .add("modifier", SerializableDataTypes.ATTRIBUTE_MODIFIER, null)
                .add("modifiers", SerializableDataTypes.ATTRIBUTE_MODIFIERS, null),
            data -> (powerType, livingEntity) -> {

                ModifyHurtTicksPower mhtp = new ModifyHurtTicksPower(
                    powerType,
                    livingEntity,
                    data.get("bientity_action"),
                    data.get("bientity_condition"),
                    data.get("damage_condition")
                );

                if (data.isPresent("modifier")) mhtp.addModifier(data.getModifier("modifier"));
                if (data.isPresent("modifiers")) {
                    List<EntityAttributeModifier> modifiers = data.get("modifiers");
                    modifiers.forEach(mhtp::addModifier);
                }

                return mhtp;

            }
        ).allowCondition();

    }

}
