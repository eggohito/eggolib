package io.github.eggohito.eggolib.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class ScoreboardUtil {

    public static int getScore(ArgumentWrapper<ScoreHolderArgumentType.ScoreHolder> scoreHolder, String objectiveName) {
        return getScore(new Pair<>(scoreHolder, objectiveName));
    }

    public static int getScore(Pair<ArgumentWrapper<ScoreHolderArgumentType.ScoreHolder>, String> scoreHolderAndObjectiveName) {

        int score = 0;
        if (Eggolib.minecraftServer == null) return score;

        ServerCommandSource source = new ServerCommandSource(
            CommandOutput.DUMMY,
            new Vec3d(0, 0, 0),
            new Vec2f(0, 0),
            Eggolib.minecraftServer.getOverworld(),
            2,
            "@",
            Text.of("@"),
            Eggolib.minecraftServer,
            null
        );

        ScoreHolderArgumentType.ScoreHolder scoreHolder = scoreHolderAndObjectiveName.getLeft().get();
        String objectiveName = scoreHolderAndObjectiveName.getRight();

        Scoreboard scoreboard = Eggolib.minecraftServer.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjective(objectiveName);

        try {
            String name = scoreHolder.getNames(source, scoreboard::getKnownPlayers).iterator().next();
            if (scoreboard.playerHasObjective(name, scoreboardObjective)) score = scoreboard.getPlayerScore(name, scoreboardObjective).getScore();
        }
        catch (CommandSyntaxException ignored) {}

        return score;

    }

}
