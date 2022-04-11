package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;

public class ScoreboardCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        String name = data.getString("name");
        String objective = data.getString("objective");
        Comparison comparison = data.get("comparison");
        int compareTo = data.getInt("compare_to");

        if (name == null) {
            if (entity instanceof PlayerEntity playerEntity) name = playerEntity.getEntityName();
            else name = entity.getUuidAsString();
        }

        Scoreboard scoreboard = entity.world.getScoreboard();
        ScoreboardObjective scoreboardObjective = scoreboard.getObjective(objective);

        if (scoreboard.playerHasObjective(name, scoreboardObjective)) {
            int score = scoreboard.getPlayerScore(name, scoreboardObjective).getScore();
            return comparison.compare(score, compareTo);
        }

        return false;
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("scoreboard"),
            new SerializableData()
                .add("name", SerializableDataTypes.STRING, null)
                .add("objective", SerializableDataTypes.STRING)
                .add("comparison", ApoliDataTypes.COMPARISON)
                .add("compare_to", SerializableDataTypes.INT),
            ScoreboardCondition::condition
        );
    }
}
