package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.component.EggolibComponents;
import io.github.eggohito.eggolib.power.GameEventListenerPower;
import io.github.eggohito.eggolib.power.InvisibilityPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.function.BiConsumer;

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

	@Inject(method = "isInvisibleTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getScoreboardTeam()Lnet/minecraft/scoreboard/AbstractTeam;"), cancellable = true)
	private void eggolib$invisibilityException(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
		if (PowerHolderComponent.hasPower((Entity) (Object) this, InvisibilityPower.class, eip -> !eip.doesApply(playerEntity))) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "updateEventHandler", at = @At("HEAD"))
	private void eggolib$updateCustomEventHandlers(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback, CallbackInfo ci) {
		if (world instanceof ServerWorld serverWorld) {
			PowerHolderComponent.getPowers((Entity) (Object) this, GameEventListenerPower.class).forEach(p -> callback.accept(p.getGameEventHandler(), serverWorld));
		}
	}

}
