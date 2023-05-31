package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.ActionOnBlockHitPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProjectileEntity.class)
public abstract class ProjectileEntityMixin extends Entity {

	@Shadow
	@Nullable
	private Entity owner;

	public ProjectileEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onBlockHit", at = @At("HEAD"))
	private void eggolib$actionOnBlockHit(BlockHitResult blockHitResult, CallbackInfo ci) {

		if (this.owner == null) {
			return;
		}

		BlockPos blockPos = blockHitResult.getBlockPos();
		Direction direction = blockHitResult.getSide();
		ProjectileEntity thisAsProjectileEntity = (ProjectileEntity) (Object) this;

		PowerHolderComponent.withPowers(
			this.owner,
			ActionOnBlockHitPower.class,
			aobhp -> aobhp.doesApply(thisAsProjectileEntity.world, blockPos, direction, thisAsProjectileEntity),
			aobhp -> aobhp.executeActions(thisAsProjectileEntity.world, blockPos, direction, thisAsProjectileEntity)
		);

	}

}
