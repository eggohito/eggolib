package io.github.eggohito.eggolib.mixin.apace100.origins;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.apace100.origins.command.LayerArgumentType;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@Mixin(LayerArgumentType.class)
public abstract class LayerArgumentTypeMixin {

    @Redirect(method = "listSuggestions", at = @At(value = "INVOKE", target = "Lnet/minecraft/command/CommandSource;suggestIdentifiers(Ljava/util/stream/Stream;Lcom/mojang/brigadier/suggestion/SuggestionsBuilder;)Ljava/util/concurrent/CompletableFuture;"))
    private CompletableFuture<Suggestions> eggolib$filterSuggestionsList(Stream<Identifier> candidates, SuggestionsBuilder builder, CommandContext<?> context) {
        return CommandSource.suggestIdentifiers(OriginLayers.getLayers().stream().filter(OriginLayer::isEnabled).map(OriginLayer::getIdentifier), builder);
    }

}
