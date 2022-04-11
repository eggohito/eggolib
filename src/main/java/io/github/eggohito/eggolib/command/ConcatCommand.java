package io.github.eggohito.eggolib.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

// TODO: Create a command that can concatenate strings from a data command storage
public class ConcatCommand {

    public static void register(CommandDispatcher<ServerCommandSource> commandDispatcher) {

        LiteralArgumentBuilder<ServerCommandSource> concatCommandBuilder = literal("concat").requires(scs -> scs.hasPermissionLevel(2));

        concatCommandBuilder
            .then(literal("test")
                .executes(ConcatCommand::test)
            );

        commandDispatcher.register(concatCommandBuilder);
    }

    private static int test(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {

        Text helloWorld = new TranslatableText("Hello world!");
        UUID uuid = commandContext.getSource().getEntityOrThrow().getUuid();
        commandContext.getSource().getServer().getPlayerManager().broadcast(helloWorld, MessageType.CHAT, uuid);

        return 1;
    }
}
