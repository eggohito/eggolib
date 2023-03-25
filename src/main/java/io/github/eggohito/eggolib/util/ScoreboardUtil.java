package io.github.eggohito.eggolib.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ScoreboardUtil {

    public static Optional<Integer> getScore(Pair<ScoreHolderArgumentType.ScoreHolder, String> scoreHolderAndObjectiveName) {
        return getScore(null, scoreHolderAndObjectiveName.getLeft(), scoreHolderAndObjectiveName.getRight());
    }

    public static Optional<Integer> getScore(@Nullable Entity invoker, ScoreHolderArgumentType.ScoreHolder scoreHolder, String objectiveName) {

        if (Eggolib.minecraftServer == null) return Optional.empty();

        ServerCommandSource source = new ServerCommandSource(
            CommandOutput.DUMMY,
            new Vec3d(0, 0, 0),
            new Vec2f(0, 0),
            Eggolib.minecraftServer.getOverworld(),
            2,
            "@",
            Text.of("@"),
            Eggolib.minecraftServer,
            invoker
        );

        Scoreboard scoreboard = Eggolib.minecraftServer.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjective(objectiveName);

        try {
            String name = scoreHolder.getNames(source, scoreboard::getKnownPlayers).iterator().next();
            if (scoreboard.playerHasObjective(name, scoreboardObjective)) return Optional.of(scoreboard.getPlayerScore(name, scoreboardObjective).getScore());
        }
        catch (CommandSyntaxException ignored) {}

        return Optional.empty();

    }

}
