package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.ModifyFovPower;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.checkerframework.common.aliasing.qual.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

	@Unique
	private float eggolib$startingFovMultiplier;

	public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Inject(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getAbilities()Lnet/minecraft/entity/player/PlayerAbilities;", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void eggolib$getStartingFov(CallbackInfoReturnable<Float> cir, float f) {
		eggolib$startingFovMultiplier = f;
	}

	@WrapOperation(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;lerp(FFF)F"))
	private float eggolib$modifyFov(float delta, float start, float end, Operation<Float> original) {

		List<ModifyFovPower> mfps = PowerHolderComponent.getPowers(this, ModifyFovPower.class);
		if (mfps.isEmpty()) {
			return original.call(delta, start, end);
		}

		boolean affectedByFovEffectScale = mfps.stream().anyMatch(ModifyFovPower::isAffectedByFovEffectScale);

		float newEnd = PowerHolderComponent.modify(this, ModifyFovPower.class, (affectedByFovEffectScale ? end : eggolib$startingFovMultiplier));
		float newDelta = affectedByFovEffectScale ? delta : start;

		return MathHelper.lerp(newDelta, start, newEnd);

	}

}
