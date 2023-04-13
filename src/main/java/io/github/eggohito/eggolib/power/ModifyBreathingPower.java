package io.github.eggohito.eggolib.power;

import dev.micalobia.breathinglib.data.BreathingInfo;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.micalobia.breathinglib.BreathingInfoBuilderAccessor;
import io.github.eggohito.eggolib.util.EntityOffset;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;

import java.util.*;
import java.util.function.Predicate;

public class ModifyBreathingPower extends PrioritizedPower {

    private final Predicate<CachedBlockPosition> breathableBlockCondition;
    private final List<StatusEffect> breathingStatusEffects;

    private final List<Modifier> gainAirModifiers;
    private final Integer gainAirInterval;
    private final List<Modifier> loseAirModifiers;
    private final Integer loseAirInterval;

    private final DamageSource damageSource;
    private final List<Modifier> damageModifiers;
    private final Integer damageInterval;

    private final ParticleEffect particle;
    private final boolean ignoreRespiration;

    public ModifyBreathingPower(PowerType<?> powerType, LivingEntity livingEntity, Predicate<CachedBlockPosition> breathableBlockCondition, StatusEffect breathingStatusEffect, List<StatusEffect> breathingStatusEffects, Modifier gainAirModifier, List<Modifier> gainAirModifiers, Integer gainAirInterval, Modifier loseAirModifier, List<Modifier> loseAirModifiers, Integer loseAirInterval, DamageSource damageSource, Modifier damageModifier, List<Modifier> damageModifiers, Integer damageInterval, ParticleEffect particle, boolean ignoreRespiration, int priority) {

        super(powerType, livingEntity, priority);
        this.breathableBlockCondition = breathableBlockCondition;

        this.breathingStatusEffects = new ArrayList<>();
        if (breathingStatusEffect != null) this.breathingStatusEffects.add(breathingStatusEffect);
        if (breathingStatusEffects != null) this.breathingStatusEffects.addAll(breathingStatusEffects);

        this.gainAirModifiers = new ArrayList<>();
        if (gainAirModifier != null) this.gainAirModifiers.add(gainAirModifier);
        if (gainAirModifiers != null) this.gainAirModifiers.addAll(gainAirModifiers);
        this.gainAirInterval = gainAirInterval;

        this.loseAirModifiers = new ArrayList<>();
        if (loseAirModifier != null) this.loseAirModifiers.add(loseAirModifier);
        if (loseAirModifiers != null) this.loseAirModifiers.addAll(loseAirModifiers);
        this.loseAirInterval = loseAirInterval;

        this.damageSource = damageSource;
        this.damageModifiers = new ArrayList<>();
        if (damageModifier != null) this.damageModifiers.add(damageModifier);
        if (damageModifiers != null) this.damageModifiers.addAll(damageModifiers);
        this.damageInterval = damageInterval;

        this.particle = particle;
        this.ignoreRespiration = ignoreRespiration;

    }

    public BreathingInfo getGainBreathInfo() {

        BreathingInfo.Builder breathingInfoBuilder = BreathingInfo.gainingAir();
        if (gainAirInterval != null) breathingInfoBuilder.airDelta(gainAirInterval);
        if (!gainAirModifiers.isEmpty()) breathingInfoBuilder.airPerCycle((int) ModifierUtil.applyModifiers(entity, gainAirModifiers, ((BreathingInfoBuilderAccessor) breathingInfoBuilder).getAirPerCycle()));

        return breathingInfoBuilder.build();

    }

    public BreathingInfo getLoseBreathInfo() {

        BreathingInfo.Builder breathingInfoBuilder = BreathingInfo.losingAir();

        if (loseAirInterval != null) breathingInfoBuilder.airDelta(loseAirInterval);
        if (damageSource != null) breathingInfoBuilder.damageSource(damageSource);
        if (damageInterval != null) breathingInfoBuilder.damageAt(damageInterval);
        if (ignoreRespiration) breathingInfoBuilder.ignoreRespiration();

        breathingInfoBuilder.particleEffect(particle);

        if (!loseAirModifiers.isEmpty()) breathingInfoBuilder.airPerCycle((int) ModifierUtil.applyModifiers(entity, loseAirModifiers, ((BreathingInfoBuilderAccessor) breathingInfoBuilder).getAirPerCycle()));
        if (!damageModifiers.isEmpty()) breathingInfoBuilder.damagePerCycle((float) ModifierUtil.applyModifiers(entity, damageModifiers, ((BreathingInfoBuilderAccessor) breathingInfoBuilder).getDamagePerCycle()));

        return breathingInfoBuilder.build();

    }

    public boolean hasBreathingStatusEffects() {
        return !breathingStatusEffects.isEmpty() && breathingStatusEffects.stream().anyMatch(entity::hasStatusEffect);
    }

    public boolean canBreatheIn(BlockPos blockPos) {
        return breathableBlockCondition.test(new CachedBlockPosition(entity.world, blockPos, true));
    }

    public static TypedActionResult<Optional<BreathingInfo>> integrateCallback(LivingEntity livingEntity) {

        List<ModifyBreathingPower> mbps = PowerHolderComponent.getPowers(livingEntity, ModifyBreathingPower.class);
        if (mbps.isEmpty()) return TypedActionResult.pass(Optional.empty());

        ModifyBreathingPower hpmbp = mbps
            .stream()
            .max(Comparator.comparing(ModifyBreathingPower::getPriority))
            .get();
        BlockPos blockPos = EntityOffset.EYES.getBlockPos(livingEntity);

        if (hpmbp.canBreatheIn(blockPos)) return TypedActionResult.success(Optional.of(hpmbp.getGainBreathInfo()));
        if (hpmbp.hasBreathingStatusEffects()) return TypedActionResult.consume(Optional.empty());
        if (livingEntity instanceof PlayerEntity playerEntity && playerEntity.getAbilities().invulnerable) return TypedActionResult.consume(Optional.empty());

        return TypedActionResult.fail(Optional.of(hpmbp.getLoseBreathInfo()));

    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("modify_breathing"),
            new SerializableData()
                .add("breathable_block_condition", ApoliDataTypes.BLOCK_CONDITION)
                .add("breathing_status_effect", SerializableDataTypes.STATUS_EFFECT, null)
                .add("breathing_status_effects", SerializableDataTypes.STATUS_EFFECTS, null)
                .add("gain_air_modifier", Modifier.DATA_TYPE, null)
                .add("gain_air_modifiers", Modifier.LIST_TYPE, null)
                .add("gain_air_interval", EggolibDataTypes.POSITIVE_INT, null)
                .add("lose_air_modifier", Modifier.DATA_TYPE, null)
                .add("lose_air_modifiers", Modifier.LIST_TYPE, null)
                .add("lose_air_interval", EggolibDataTypes.POSITIVE_INT, null)
                .add("damage_source", SerializableDataTypes.DAMAGE_SOURCE, null)
                .add("damage_modifier", Modifier.DATA_TYPE, null)
                .add("damage_modifiers", Modifier.LIST_TYPE, null)
                .add("damage_interval", EggolibDataTypes.POSITIVE_INT, null)
                .add("particle", SerializableDataTypes.PARTICLE_EFFECT_OR_TYPE, null)
                .add("ignore_respiration", SerializableDataTypes.BOOLEAN, false)
                .add("priority", SerializableDataTypes.INT, 0),
            data -> (powerType, livingEntity) -> new ModifyBreathingPower(
                powerType,
                livingEntity,
                data.get("breathable_block_condition"),
                data.get("breathing_status_effect"),
                data.get("breathing_status_effects"),
                data.get("gain_air_modifier"),
                data.get("gain_air_modifiers"),
                data.get("gain_air_interval"),
                data.get("lose_air_modifier"),
                data.get("lose_air_modifiers"),
                data.get("lose_air_interval"),
                data.get("damage_source"),
                data.get("damage_modifier"),
                data.get("damage_modifiers"),
                data.get("damage_interval"),
                data.get("particle"),
                data.get("ignore_respiration"),
                data.get("priority")
            )
        ).allowCondition();
    }

}
