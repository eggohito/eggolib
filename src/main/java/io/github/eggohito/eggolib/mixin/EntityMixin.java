package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.component.EggolibEntityComponents;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Entity.class)
public class EntityMixin {

    @Inject(method = "addScoreboardTag", at = @At("TAIL"))
    private void eggolib$syncScoreboardTagsOnAdd(String tag, CallbackInfoReturnable<Boolean> cir) {

        if (cir.getReturnValue()) EggolibEntityComponents.MISC.maybeGet(this).ifPresent(
            miscComponent -> {
                miscComponent.addScoreboardTag(tag);
                miscComponent.sync();
            }
        );

    }

    @Inject(method = "removeScoreboardTag", at = @At("TAIL"))
    private void eggolib$syncScoreboardTagsOnRemove(String tag, CallbackInfoReturnable<Boolean> cir) {

        if (cir.getReturnValue()) EggolibEntityComponents.MISC.maybeGet(this).ifPresent(
            miscComponent -> {
                miscComponent.removeScoreboardTag(tag);
                miscComponent.sync();
            }
        );

    }

    @Inject(method = "getScoreboardTags", at = @At(value = "TAIL"))
    private void eggolib$syncScoreboardTags(CallbackInfoReturnable<Set<String>> cir) {

        EggolibEntityComponents.MISC.maybeGet(this).ifPresent(
            miscComponent -> {
                miscComponent.copyScoreboardTagsFrom(cir.getReturnValue());
                miscComponent.sync();
            }
        );

    }

}
