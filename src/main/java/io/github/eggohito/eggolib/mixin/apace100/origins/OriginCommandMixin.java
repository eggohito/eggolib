package io.github.eggohito.eggolib.mixin.apace100.origins;

import com.mojang.brigadier.context.CommandContext;
import io.github.apace100.origins.command.OriginCommand;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collection;

@Mixin(OriginCommand.class)
public abstract class OriginCommandMixin {

    @Unique private static int eggolib$rejectedTargets;

    @Inject(method = "setOrigin", at = @At(value = "INVOKE", target = "Lio/github/apace100/origins/component/OriginComponent;setOrigin(Lio/github/apace100/origins/origin/OriginLayer;Lio/github/apace100/origins/origin/Origin;)V"), remap = false, cancellable = true)
    private static void eggolib$onlySetRegisteredOrigin(PlayerEntity player, OriginLayer layer, Origin origin, CallbackInfo ci) {
        if (!(origin.equals(Origin.EMPTY) || layer.getOrigins().contains(origin.getIdentifier()))) {
            eggolib$rejectedTargets++;
            ci.cancel();
        }
    }

    @Inject(method = "lambda$register$1", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void eggolib$setProcessedTargetsValue(CommandContext<?> command, CallbackInfoReturnable<Integer> cir, int i, Collection<?> targets, OriginLayer l, Origin o) {
        if (eggolib$rejectedTargets > 0 && command.getSource() instanceof ServerCommandSource serverCommandSource) serverCommandSource.sendError(Text.translatable("commands.origin.unregistered_in_layer", o.getIdentifier(), l.getIdentifier()));
        int j = i - eggolib$rejectedTargets;
        eggolib$rejectedTargets = 0;
        cir.setReturnValue(j);
    }

    @Inject(method = "hasOrigin", at = @At("RETURN"), remap = false, cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private static void eggolib$allowEmptyCaseForHas(PlayerEntity player, OriginLayer layer, Origin origin, CallbackInfoReturnable<Boolean> cir, OriginComponent component) {
        cir.setReturnValue((origin.equals(Origin.EMPTY) || component.hasOrigin(layer)) && component.getOrigin(layer).equals(origin));
    }

}
