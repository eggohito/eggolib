package io.github.eggohito.eggolib.mixin.origins;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.apace100.origins.command.OriginArgumentType;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Mixin(OriginArgumentType.class)
public abstract class OriginArgumentTypeMixin {

    @Redirect(method = "listSuggestions", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/CommandSource;suggestIdentifiers(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Suggestions> eggolib$onlyListRegisteredOrigins(Stream<Identifier> candidates, SuggestionsBuilder builder, CommandContext<?> context) {

        List<Identifier> availableOrigins = new ArrayList<>();

        try {
            Identifier originLayerId = context.getArgument("layer", Identifier.class);
            OriginLayer originLayer = OriginLayers.getLayer(originLayerId);

            availableOrigins.add(Origin.EMPTY.getIdentifier());
            if (originLayer != null) availableOrigins.addAll(originLayer.getOrigins());
        }

        catch (IllegalArgumentException ignored) {}

        return CommandSource.suggestIdentifiers(availableOrigins.stream(), builder);

    }

}
