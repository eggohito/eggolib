package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.ModifyBouncinessPower;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {

	@Unique
	private boolean eggolib$shouldBounceEntity = false;

	@Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
	private void eggolib$onLanding(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {

		if (!PowerHolderComponent.hasPower(entity, ModifyBouncinessPower.class, mbp -> mbp.doesApply(world, pos))) {
			return;
		}

		eggolib$shouldBounceEntity = true;
		ci.cancel();

	}

	@Inject(method = "onEntityLand", at = @At("HEAD"), cancellable = true)
	private void eggolib$modifyBounciness(BlockView world, Entity entity, CallbackInfo ci) {

		if (!eggolib$shouldBounceEntity) {
			return;
		}
		eggolib$shouldBounceEntity = false;

		double yMultiplier = PowerHolderComponent.modify(entity, ModifyBouncinessPower.class, 0.8, ModifyBouncinessPower::doesApply, ModifyBouncinessPower::executeActions);
		entity.setVelocity(entity.getVelocity().multiply(1.0, -yMultiplier, 1.0));
		ci.cancel();

	}

}
