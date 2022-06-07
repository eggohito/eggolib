package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public abstract class GameOptionsMixin {

    @Shadow protected MinecraftClient client;

    @Inject(method = "setPerspective", at = @At("HEAD"))
    private void eggolib$getCurrentPerspective(Perspective perspective, CallbackInfo ci) {

        if (this.client.player == null) return;
        EggolibPerspective eggolibPerspective = null;

        switch (perspective) {
            case FIRST_PERSON:
                eggolibPerspective = EggolibPerspective.FIRST_PERSON;
                break;
            case THIRD_PERSON_BACK:
                eggolibPerspective = EggolibPerspective.THIRD_PERSON_BACK;
                break;
            case THIRD_PERSON_FRONT:
                eggolibPerspective = EggolibPerspective.THIRD_PERSON_FRONT;
                break;
        }

        Eggolib.PLAYERS_CURRENT_PERSPECTIVE.put(this.client.player, eggolibPerspective.toString());

    }

}
