package io.github.eggohito.eggolib.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

	@Shadow
	public abstract @Nullable Entity getHolder();

	@Shadow
	public abstract void setHolder(@Nullable Entity holder);

	@Shadow
	private @Nullable Entity holder;

	@Inject(method = "inventoryTick", at = @At("HEAD"))
	private void eggolib$setHolderOnTick(World world, Entity entity, int slot, boolean selected, CallbackInfo ci) {
		if (getHolder() == null) {
			setHolder(entity);
		}
	}

	@Inject(method = "copy", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setBobbingAnimationTime(I)V"), locals = LocalCapture.CAPTURE_FAILHARD)
	private void eggolib$setHolderOnCopy(CallbackInfoReturnable<ItemStack> cir, ItemStack itemStack) {
		itemStack.setHolder(this.holder);
	}

}
