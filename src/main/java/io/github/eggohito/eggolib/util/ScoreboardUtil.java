package io.github.eggohito.eggolib.util;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.util.Pair;

public class ScoreboardUtil {

    public static int getScore(Pair<String, String> nameAndObjective) {

        String name = nameAndObjective.getLeft();
        String objective = nameAndObjective.getRight();

        if (Eggolib.minecraftServer == null) return 0;

        Scoreboard scoreboard = Eggolib.minecraftServer.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjective(objective);

        if (scoreboard.playerHasObjective(name, scoreboardObjective)) return scoreboard.getPlayerScore(name, scoreboardObjective).getScore();
        else return 0;

    }

}
