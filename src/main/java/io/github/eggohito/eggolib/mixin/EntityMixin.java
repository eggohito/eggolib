package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.component.entity.MiscComponent;
import io.github.eggohito.eggolib.power.EggolibInvisibilityPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Entity.class)
public class EntityMixin {

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

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void eggolib$invisibility(CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower((Entity) (Object) this, EggolibInvisibilityPower.class)) cir.setReturnValue(true);
    }

    @Inject(method = "isInvisibleTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getScoreboardTeam()Lnet/minecraft/scoreboard/AbstractTeam;"), cancellable = true)
    private void eggolib$invisibilityException(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        if (PowerHolderComponent.hasPower((Entity) (Object) this, EggolibInvisibilityPower.class, eip -> !eip.doesApply(playerEntity))) cir.setReturnValue(false);
    }

}
