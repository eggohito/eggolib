package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyDamageDealtPower;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import io.github.eggohito.eggolib.power.ActionOnCriticalHitPower;
import io.github.eggohito.eggolib.power.PreventCriticalHitPower;
import io.github.eggohito.eggolib.power.PrioritizedPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = PlayerEntity.class, priority = 1500)
public abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandOutput {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damage", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
    private void eggolib$allowDamageIfModifyingPowersExist(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        boolean hasModifyingPower = false;

        if (source.getAttacker() != null) {
            if (!source.isProjectile()) hasModifyingPower = PowerHolderComponent
                .hasPower(
                    source.getAttacker(),
                    ModifyDamageDealtPower.class,
                    mddp -> mddp.doesApply(source, amount, this)
                );
            else hasModifyingPower = PowerHolderComponent
                .hasPower(
                    source.getAttacker(),
                    ModifyProjectileDamagePower.class,
                    mpdp -> mpdp.doesApply(source, amount, this)
                );
        }

        hasModifyingPower |= PowerHolderComponent
            .hasPower(
                this,
                ModifyDamageTakenPower.class,
                mdtp -> mdtp.doesApply(source, amount)
            );

        if (hasModifyingPower) cir.setReturnValue(super.damage(source, amount));

    }

    @Unique private Entity eggolib$cachedTarget;
    @Unique private float eggolib$cachedDamageAmount;
    @Unique private DamageSource eggolib$cachedDamageSource;

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void eggolib$cacheVars(Entity target, CallbackInfo ci, float f, float g, float h, boolean bl, boolean bl2, int i, boolean bl3) {
        eggolib$cachedTarget = target;
        eggolib$cachedDamageAmount = f;
        eggolib$cachedDamageSource = DamageSource.player((PlayerEntity) (Object) this);
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z", ordinal = 1))
    private boolean eggolib$preventCriticalHit(PlayerEntity instance) {

        PrioritizedPower.SortedMap<PreventCriticalHitPower> pchpsm = new PrioritizedPower.SortedMap<>();
        pchpsm.add(this, PreventCriticalHitPower.class, pchp -> pchp.doesApply(eggolib$cachedTarget, eggolib$cachedDamageSource, eggolib$cachedDamageAmount));

        int j = 0;
        for (int i = pchpsm.getMaxPriority(); i >= 0; i--) {
            if (!pchpsm.hasPowers(i)) continue;
            pchpsm.getPowers(i).forEach(pchp -> pchp.executeActions(eggolib$cachedTarget));
            j++;
        }

        if (j > 0) return true;
        else return instance.isSprinting();

    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
    private void eggolib$actionOnCriticalHit(Entity target, CallbackInfo ci) {

        PrioritizedPower.SortedMap<ActionOnCriticalHitPower> aochpsm = new PrioritizedPower.SortedMap<>();
        aochpsm.add(this, ActionOnCriticalHitPower.class, aochp -> aochp.doesApply(eggolib$cachedDamageSource, eggolib$cachedDamageAmount, eggolib$cachedTarget));

        for (int i = aochpsm.getMaxPriority(); i >= 0; i--) {
            if (!aochpsm.hasPowers(i)) continue;
            aochpsm.getPowers(i).forEach(aochp -> aochp.executeActions(target));
        }

    }

}
