package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.eggohito.eggolib.component.EggolibComponents;
import io.github.eggohito.eggolib.power.ModifyPassengerPositionPower;
import io.github.eggohito.eggolib.power.ModifyRidingPositionPower;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Entity.class)
public abstract class EntityMixin {

	@Shadow
	private World world;

	@Inject(method = "addCommandTag", at = @At("TAIL"))
	private void eggolib$onAddCommandTag(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			EggolibComponents.MISC.get(this).addCommandTag(tag);
		}
	}

	@Inject(method = "removeScoreboardTag", at = @At("TAIL"))
	private void eggolib$onRemoveCommandTag(String tag, CallbackInfoReturnable<Boolean> cir) {
		EggolibComponents.MISC.get(this).removeCommandTag(tag);
	}

	@Inject(method = "getCommandTags", at = @At("RETURN"))
	private void eggolib$onGetCommandTag(CallbackInfoReturnable<Set<String>> cir) {
		EggolibComponents.MISC.get(this).setCommandTags(cir.getReturnValue());
	}

	@Inject(method = "baseTick", at = @At("TAIL"))
	private void eggolib$syncCommandTags(CallbackInfo ci) {

		if (!this.world.isClient) {
			EggolibComponents.MISC.get(this).sync(false);
		}

	}

	@ModifyExpressionValue(method = "getPassengerRidingPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getPassengerAttachmentPos(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/EntityDimensions;F)Lorg/joml/Vector3f;"))
	private Vector3f eggolib$modifyRidingPosition(Vector3f original, Entity passenger) {
		Vector3f modified = ModifyPassengerPositionPower.modify((Entity) (Object) this, passenger, original);
		return ModifyRidingPositionPower.modify(passenger, (Entity) (Object) this, modified);
	}

}
