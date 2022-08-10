package io.github.eggohito.eggolib.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.apace100.origins.command.LayerArgumentType;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.command.CommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EggolibOriginArgumentType implements ArgumentType<Identifier> {

    public static final DynamicCommandExceptionType ORIGIN_NOT_FOUND = new DynamicCommandExceptionType(
        o -> Text.translatable("commands.origin.origin_not_found", o)
    );

    public static final DynamicCommandExceptionType LAYER_NOT_FOUND = new DynamicCommandExceptionType(
        o -> Text.translatable("commands.origin.layer_not_found", o)
    );

    public static EggolibOriginArgumentType origin() {
        return new EggolibOriginArgumentType();
    }

    public static Origin getOrigin(CommandContext<ServerCommandSource> commandContext, String origin) throws CommandSyntaxException {

        Identifier originId = commandContext.getArgument(origin, Identifier.class);

        try {
            return OriginRegistry.get(originId);
        }

        catch (IllegalArgumentException e) {
            throw ORIGIN_NOT_FOUND.create(originId);
        }

    }

    @Override
    public Identifier parse(StringReader stringReader) throws CommandSyntaxException {
        return Identifier.fromCommandInput(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        List<Identifier> availableOrigins = new ArrayList<>();
        availableOrigins.add(Origin.EMPTY.getIdentifier());

        Identifier originLayerId = context.getArgument("layer", Identifier.class);
        OriginLayer originLayer = OriginLayers.getLayer(originLayerId);
        if (originLayer != null) availableOrigins.addAll(originLayer.getOrigins());

        return CommandSource.suggestIdentifiers(availableOrigins.stream(), builder);

    }

}
