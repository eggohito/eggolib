package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.component.EggolibComponents;
import io.github.eggohito.eggolib.power.InvisibilityPower;
import io.github.eggohito.eggolib.power.GameEventListenerPower;
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
	public World world;

	@Inject(method = "addCommandTag", at = @At("TAIL"))
	private void eggolib$syncScoreboardTagsOnAdd(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			EggolibComponents.MISC.maybeGet(this).ifPresent(miscComponent -> miscComponent.addScoreboardTag(tag));
		}
	}

	@Inject(method = "removeScoreboardTag", at = @At("TAIL"))
	private void eggolib$syncScoreboardTagsOnRemove(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			EggolibComponents.MISC.maybeGet(this).ifPresent(miscComponent -> miscComponent.removeScoreboardTag(tag));
		}
	}

	@Inject(method = "getCommandTags", at = @At(value = "TAIL"))
	private void eggolib$syncScoreboardTags(CallbackInfoReturnable<Set<String>> cir) {
		EggolibComponents.MISC.maybeGet(this).ifPresent(miscComponent -> miscComponent.copyScoreboardTagsFrom(cir.getReturnValue()));
	}

	@Inject(method = "isInvisibleTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getScoreboardTeam()Lnet/minecraft/scoreboard/AbstractTeam;"), cancellable = true)
	private void eggolib$invisibilityException(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
		if (PowerHolderComponent.hasPower((Entity) (Object) this, InvisibilityPower.class, eip -> !eip.doesApply(playerEntity))) {
			cir.setReturnValue(false);
		}
	}

	@Inject(method = "updateEventHandler", at = @At("HEAD"))
	private void eggolib$updateGameEventListenerPowerHandlers(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback, CallbackInfo ci) {

		if (!(world instanceof ServerWorld serverWorld)) {
			return;
		}

		PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(this).orElse(null);
		if (component == null) {
			return;
		}

		for (GameEventListenerPower gelp : component.getPowers(GameEventListenerPower.class)) {
			if (gelp.canListen()) {
				callback.accept(gelp.getGameEventHandler(), serverWorld);
			}
		}

	}

}
