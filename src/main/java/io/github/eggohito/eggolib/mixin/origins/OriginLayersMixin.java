package io.github.eggohito.eggolib.mixin.origins;

import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;

@Mixin(OriginLayers.class)
public abstract class OriginLayersMixin {

    @Shadow(remap = false) @Final private static HashMap<Identifier, OriginLayer> layers;

    @Inject(method = "getLayer", at = @At("HEAD"))
    private static void eggolib$nonExistentLayerException(Identifier id, CallbackInfoReturnable<OriginLayer> cir) {
        if (!layers.containsKey(id)) throw new IllegalArgumentException("Could not get layer from id '" + id.toString() + "', as it does not exist!");
    }

}
