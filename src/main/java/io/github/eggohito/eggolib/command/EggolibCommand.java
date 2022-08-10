package io.github.eggohito.eggolib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.origins.command.LayerArgumentType;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginRegistry;
import io.github.apace100.origins.registry.ModComponents;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.command.argument.EggolibOriginArgumentType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class EggolibCommand {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {

        LiteralArgumentBuilder<ServerCommandSource> eggolibCommand = literal("eggolib").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2));

        commandDispatcher.register(
            eggolibCommand
                .then(
                    literal("test")
                        .executes(EggolibCommand::test)
                )
        );

        if (FabricLoader.getInstance().isModLoaded("origins")) commandDispatcher.register(
            eggolibCommand
                .then(
                    literal("origin")
                        .then(
                            literal("random")
                                .then(
                                    argument(
                                        "targets",
                                        EntityArgumentType.players()
                                    )
                                        .then(
                                            argument(
                                                "layer",
                                                LayerArgumentType.layer()
                                            )
                                                .executes(EggolibCommand::randomizeOrigin)
                                        )
                                )
                        )
                        .then(
                            literal("set")
                                .then(
                                    argument(
                                        "targets",
                                        EntityArgumentType.players()
                                    )
                                        .then(
                                            argument(
                                                "layer",
                                                LayerArgumentType.layer()
                                            )
                                                .then(
                                                    argument(
                                                        "origin",
                                                        EggolibOriginArgumentType.origin()
                                                    )
                                                        .executes(EggolibCommand::setOrigin)
                                                )
                                        )
                                )
                        )
                )
        );

    }

    private static int test(CommandContext<ServerCommandSource> commandContext) {
        commandContext.getSource().sendFeedback(Text.of("Hello world!"), true);
        return 3660718; // Number speak for "EGGOLIB" :P
    }
    
    private static int setOrigin(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        
        Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(commandContext, "targets");
        OriginLayer originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        Origin origin = EggolibOriginArgumentType.getOrigin(commandContext, "origin");
        ServerCommandSource serverCommandSource = commandContext.getSource();
        
        int processedTargets = 0;
        
        if (originLayer.getOrigins().contains(origin.getIdentifier())) {
            
            for (ServerPlayerEntity target : targets) {
                
                OriginComponent originComponent = ModComponents.ORIGIN.get(target);
                boolean hadOriginBefore = originComponent.hadOriginBefore();
                
                originComponent.setOrigin(originLayer, origin);
                originComponent.sync();
                
                OriginComponent.partialOnChosen(target, hadOriginBefore, origin);
                
                processedTargets++;
                
            }
            
            if (processedTargets == 1) serverCommandSource.sendFeedback(Text.translatable("commands.origin.set.success.single", targets.iterator().next().getDisplayName().getString(), Text.translatable(originLayer.getTranslationKey()), origin.getName()), true);
            else serverCommandSource.sendFeedback(Text.translatable("commands.origin.set.success.multiple", processedTargets, Text.translatable(originLayer.getTranslationKey()), origin.getName()), true);
            
        }
        
        else serverCommandSource.sendError(Text.translatable("commands.origin.origin_not_found", origin.getIdentifier()));
        
        return processedTargets;
        
    }

    private static int randomizeOrigin(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {

        Collection<ServerPlayerEntity> targets = EntityArgumentType.getPlayers(commandContext, "targets");
        OriginLayer originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        ServerCommandSource serverCommandSource = commandContext.getSource();

        Entity sourceEntity = serverCommandSource.getEntity();
        Entity targetEntity = targets.iterator().next();
        Origin origin = null;

        int processedTargets = 0;

        if (originLayer.isRandomAllowed()) {

            for (ServerPlayerEntity target : targets) {

                OriginComponent originComponent = ModComponents.ORIGIN.get(target);
                List<Origin> randomOrigins = originLayer.getRandomOrigins(target).stream().map(OriginRegistry::get).toList();
                origin = randomOrigins.get(new Random().nextInt(randomOrigins.size()));

                boolean hadOriginBefore = originComponent.hadOriginBefore();
                boolean hadAllOrigins = originComponent.hasAllOrigins();

                originComponent.setOrigin(originLayer, origin);
                originComponent.checkAutoChoosingLayers(target, false);
                originComponent.sync();

                if (originComponent.hasAllOrigins() && !hadAllOrigins) OriginComponent.onChosen(target, hadOriginBefore);

                Eggolib.LOGGER.info(
                    "Player {} was randomly assigned the following Origin: {} for layer: {}",
                    target.getDisplayName().getString(),
                    origin.getIdentifier().toString(),
                    originLayer.getIdentifier().toString()
                );

                target.sendMessage(
                    Text.translatable(
                        "commands.eggolib.randomize_origin.success.to_target",
                        origin.getName(),
                        Text.translatable(originLayer.getTranslationKey())
                    ),
                    false
                );

                processedTargets++;

            }

            if (processedTargets == 1) {
                if (sourceEntity != null && !sourceEntity.equals(targetEntity)) serverCommandSource.sendFeedback(Text.translatable("commands.eggolib.randomize_origin.success.single", targetEntity.getDisplayName().getString(), origin.getName(), Text.translatable(originLayer.getTranslationKey())), true);
            }
            else serverCommandSource.sendFeedback(Text.translatable("commands.eggolib.randomize_origin.success.multiple", processedTargets, Text.translatable(originLayer.getTranslationKey())), true);

        }

        else {
            if (targets.size() == 1) serverCommandSource.sendFeedback(Text.translatable("commands.eggolib.randomize_origin.fail.single", targetEntity.getDisplayName().getString(), Text.translatable(originLayer.getTranslationKey()), Text.translatable("layer.origins.random_not_allowed")), true);
            else serverCommandSource.sendFeedback(Text.translatable("commands.eggolib.randomize_origin.fail.multiple", targets.size(), Text.translatable(originLayer.getTranslationKey()), Text.translatable("layer.origins.random_not_allowed")), true);
        }

        return processedTargets;

    }

}
