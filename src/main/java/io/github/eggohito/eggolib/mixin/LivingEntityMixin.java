package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.eggohito.eggolib.power.ModifyHurtTicksPower;
import io.github.eggohito.eggolib.power.ModifyPassengerPositionPower;
import io.github.eggohito.eggolib.power.ModifyRidingPositionPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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

	@ModifyExpressionValue(method = "getPassengerRidingPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPassengerAttachmentPos(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EntityDimensions;F)Lorg/joml/Vector3f;"))
	private Vector3f eggolib$modifyRidingPosition(Vector3f original, Entity passenger) {
		Vector3f modified = ModifyPassengerPositionPower.modify(this, passenger, original);
		return ModifyRidingPositionPower.modify(passenger, this, modified);
	}


}
