package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.eggohito.eggolib.component.EggolibComponents;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@SuppressWarnings("unused")
@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	private World world;

	@Shadow
	@Final
	private Set<String> commandTags;

	@Unique
	private boolean eggolib$syncCommandTags;

	@Inject(method = "addCommandTag", at = @At("TAIL"))
	private void eggolib$addAndSyncCommandTag(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue() && EggolibComponents.MISC.get(this).addCommandTag(tag)) {
			eggolib$syncCommandTags = true;
		}
	}

	@Inject(method = "removeScoreboardTag", at = @At("TAIL"))
	private void eggolib$removeAndSyncCommandTag(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (EggolibComponents.MISC.get(this).removeCommandTag(tag)) {
			eggolib$syncCommandTags = true;
		}
	}

	@ModifyReturnValue(method = "getCommandTags", at = @At(value = "RETURN"))
	private Set<String> eggolib$overrideGetCommandTags(Set<String> original) {
		return EggolibComponents.MISC.get(this).getCommandTags();
	}

	@Inject(method = "baseTick", at = @At("TAIL"))
	private void eggolib$syncCommandTags(CallbackInfo ci) {

		if (this.world.isClient || !eggolib$syncCommandTags) {
			return;
		}

		EggolibComponents.MISC.get(this).sync();
		eggolib$syncCommandTags = false;

	}

}
