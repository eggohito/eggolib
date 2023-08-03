package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.eggohito.eggolib.power.InvisibilityPower;
import io.github.eggohito.eggolib.power.ModifyHurtTicksPower;
import io.github.eggohito.eggolib.util.InventoryUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	public LivingEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@ModifyExpressionValue(method = "damage", at = @At(value = "CONSTANT", args = "floatValue=10.0f", ordinal = 0))
	private float eggolib$modifyImmunityTicks(float original, DamageSource source, float amount) {

		LivingEntity thisAsLiving = (LivingEntity) (Object) this;
		List<ModifyHurtTicksPower> mhtps = PowerHolderComponent.getPowers(thisAsLiving, ModifyHurtTicksPower.class)
			.stream()
			.filter(mhtp -> mhtp.doesApply(source, amount, source.getAttacker()))
			.toList();

		if (mhtps.isEmpty()) {
			return original;
		}
		return (float) ModifierUtil.applyModifiers(thisAsLiving, mhtps.stream().flatMap(mhtp -> mhtp.getImmunityModifiers().stream()).toList(), original);

	}

	@ModifyExpressionValue(method = "damage", at = @At(value = "CONSTANT", args = "intValue=20"))
	private int eggolib$modifyHurtTicks(int original, DamageSource source, float amount) {

		LivingEntity thisAsLiving = (LivingEntity) (Object) this;
		List<ModifyHurtTicksPower> mhtps = PowerHolderComponent.getPowers(thisAsLiving, ModifyHurtTicksPower.class)
			.stream()
			.filter(mhtp -> mhtp.doesApply(source, amount, source.getAttacker()))
			.toList();

		if (mhtps.isEmpty()) {
			return original;
		}

		mhtps.forEach(mhtp -> mhtp.executeActions(source.getAttacker()));
		return (int) ModifierUtil.applyModifiers(thisAsLiving, mhtps.stream().flatMap(mhtp -> mhtp.getModifiers().stream()).toList(), original);

	}

	@ModifyExpressionValue(method = "getAttackDistanceScalingFactor", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInvisible()Z"))
	private boolean eggolib$invisibilityException(boolean original, Entity entity) {
		return entity == null || !PowerHolderComponent.hasPower(this, InvisibilityPower.class) ? original :
		       PowerHolderComponent.hasPower(this, InvisibilityPower.class, p -> p.doesApply(entity));
	}

	@Inject(method = "baseTick", at = @At("TAIL"))
	private void eggolib$setHolderOfStacks(CallbackInfo ci) {
		InventoryUtil.forEachStack(this, stack -> stack.setHolder(this));
	}

}
