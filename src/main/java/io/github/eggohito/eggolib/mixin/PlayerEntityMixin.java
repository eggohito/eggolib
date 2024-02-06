package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Prioritized;
import io.github.eggohito.eggolib.power.*;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Comparator;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandOutput {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Unique
	private Entity eggolib$cachedTarget;
	@Unique
	private float eggolib$cachedDamageAmount;
	@Unique
	private DamageSource eggolib$cachedDamageSource;

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
	private void eggolib$cacheVars(Entity target, CallbackInfo ci, float f, float g, float h, boolean bl, boolean bl2, int i, boolean bl3) {
		eggolib$cachedTarget = target;
		eggolib$cachedDamageAmount = f;
		eggolib$cachedDamageSource = this.getDamageSources().playerAttack((PlayerEntity) (Object) this);
	}

	@ModifyExpressionValue(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isSprinting()Z", ordinal = 1))
	private boolean eggolib$preventCriticalHit(boolean originalValue) {

		Prioritized.CallInstance<PrioritizedPower> pchpci = new Prioritized.CallInstance<>();
		pchpci.add(this, PreventCriticalHitPower.class, pchp -> pchp.doesApply(eggolib$cachedTarget, eggolib$cachedDamageSource, eggolib$cachedDamageAmount));

		int preventCriticalHitPowers = 0;
		for (int i = pchpci.getMaxPriority(); i >= pchpci.getMinPriority(); i--) {

			if (!pchpci.hasPowers(i)) {
				continue;
			}

			List<PreventCriticalHitPower> pchps = pchpci.getPowers(i)
				.stream()
				.filter(p -> p instanceof PreventCriticalHitPower)
				.map(p -> (PreventCriticalHitPower) p)
				.toList();

			preventCriticalHitPowers += pchps.size();
			pchps.forEach(pchp -> pchp.executeActions(eggolib$cachedTarget));

		}

		if (preventCriticalHitPowers > 0) {
			return true;
		} else {
			return originalValue;
		}

	}

	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"))
	private void eggolib$actionOnCriticalHit(Entity target, CallbackInfo ci) {

		Prioritized.CallInstance<PrioritizedPower> aochpci = new Prioritized.CallInstance<>();
		aochpci.add(this, ActionOnCriticalHitPower.class, aochp -> aochp.doesApply(eggolib$cachedDamageSource, eggolib$cachedDamageAmount, eggolib$cachedTarget));

		for (int i = aochpci.getMaxPriority(); i >= aochpci.getMinPriority(); i--) {

			if (!aochpci.hasPowers(i)) {
				continue;
			}

			aochpci.getPowers(i)
				.stream()
				.filter(p -> p instanceof ActionOnCriticalHitPower)
				.map(p -> (ActionOnCriticalHitPower) p)
				.forEach(aochp -> aochp.executeActions(target));

		}

	}

	@Inject(method = "updatePose", at = @At("HEAD"), cancellable = true)
	private void eggolib$forcePose(CallbackInfo ci) {

		PowerHolderComponent.getPowers(this, PosePower.class)
			.stream()
			.max(Comparator.comparing(PosePower::getPriority))
			.ifPresent(p -> {
				this.setPose(p.getPose());
				ci.cancel();
			});

	}

}
