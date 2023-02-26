package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.component.entity.MiscComponent;
import io.github.eggohito.eggolib.power.EggolibInvisibilityPower;
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

    @Shadow public World world;

    @Inject(method = "addScoreboardTag", at = @At("TAIL"))
    private void eggolib$syncScoreboardTagsOnAdd(String tag, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) MiscComponent.KEY.maybeGet(this).ifPresent(miscComponent -> miscComponent.addScoreboardTag(tag));
    }

    @Inject(method = "removeScoreboardTag", at = @At("TAIL"))
    private void eggolib$syncScoreboardTagsOnRemove(String tag, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) MiscComponent.KEY.maybeGet(this).ifPresent(miscComponent -> miscComponent.removeScoreboardTag(tag));
    }

    @Inject(method = "getScoreboardTags", at = @At(value = "TAIL"))
    private void eggolib$syncScoreboardTags(CallbackInfoReturnable<Set<String>> cir) {
        MiscComponent.KEY.maybeGet(this).ifPresent(miscComponent -> miscComponent.copyScoreboardTagsFrom(cir.getReturnValue()));
    }

    @Inject(method = "isInvisibleTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getScoreboardTeam()Lnet/minecraft/scoreboard/AbstractTeam;"), cancellable = true)
    private void eggolib$invisibilityException(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower((Entity) (Object) this, EggolibInvisibilityPower.class, eip -> !eip.doesApply(playerEntity))) cir.setReturnValue(false);
    }

    @Inject(method = "updateEventHandler", at = @At("HEAD"))
    private void eggolib$updateGameEventListenerPowerHandlers(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback, CallbackInfo ci) {

        if (!(world instanceof ServerWorld serverWorld)) return;

        Entity thisAsEntity = (Entity) (Object) this;
        PowerHolderComponent.KEY.maybeGet(thisAsEntity).ifPresent(
            powerHolderComponent -> powerHolderComponent
                .getPowers(GameEventListenerPower.class)
                .stream()
                .filter(GameEventListenerPower::canListen)
                .forEach(gelp -> callback.accept(gelp.getGameEventHandler(), serverWorld))
        );

    }

}
