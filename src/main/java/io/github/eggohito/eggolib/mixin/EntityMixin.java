package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.component.EggolibComponents;
import io.github.eggohito.eggolib.component.entity.IMiscComponent;
import io.github.eggohito.eggolib.power.GameEventListenerPower;
import io.github.eggohito.eggolib.power.InvisibilityPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import org.spongepowered.asm.mixin.Final;
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

	@Shadow
	@Final
	private Set<String> commandTags;

	@Inject(method = "addCommandTag", at = @At("TAIL"))
	private void eggolib$syncScoreboardTagsOnAdd(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (!world.isClient && cir.getReturnValue()) {
			EggolibComponents.MISC.maybeGet(this).ifPresent(miscComponent -> miscComponent.addScoreboardTag(tag));
		}
	}

	@Inject(method = "removeScoreboardTag", at = @At("TAIL"))
	private void eggolib$syncScoreboardTagsOnRemove(String tag, CallbackInfoReturnable<Boolean> cir) {
		if (!world.isClient && cir.getReturnValue()) {
			EggolibComponents.MISC.maybeGet(this).ifPresent(miscComponent -> miscComponent.removeScoreboardTag(tag));
		}
	}

	@ModifyExpressionValue(method = "getCommandTags", at = @At(value = "HEAD"))
	private Set<String> eggolib$syncAndGetCommandTags() {

		IMiscComponent component = EggolibComponents.MISC.get(this);
		if (!world.isClient) {
			component.copyScoreboardTagsFrom(this.commandTags);
		}

		return component.getScoreboardTags();

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
