package io.github.eggohito.eggolib.condition.bientity;

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
import net.minecraft.util.Pair;

public class CompareScoreCondition {

    public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

        if (actorAndTarget.getLeft() == null || actorAndTarget.getRight() == null) return false;

        String actorName = getUuidOrNameString(actorAndTarget.getLeft());
        String targetName = getUuidOrNameString(actorAndTarget.getRight());
        String actorObjectiveName = data.getString("actor_objective");
        String targetObjectiveName = data.getString("target_objective");

        Comparison comparison = data.get("comparison");
        Scoreboard scoreboard = actorAndTarget.getLeft().world.getScoreboard();

        ScoreboardObjective actorObjective = scoreboard.getObjective(actorObjectiveName);
        ScoreboardObjective targetObjective = scoreboard.getObjective(targetObjectiveName);

        if (!(scoreboard.playerHasObjective(actorName, actorObjective) || scoreboard.playerHasObjective(targetName, targetObjective))) return false;
        return comparison.compare(scoreboard.getPlayerScore(actorName, actorObjective).getScore(), scoreboard.getPlayerScore(targetName, targetObjective).getScore());

    }

    private static String getUuidOrNameString(Entity entity) {
        return entity instanceof PlayerEntity playerEntity ? playerEntity.getEntityName() : entity.getUuidAsString();
    }

    public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("compare_score"),
            new SerializableData()
                .add("actor_objective", SerializableDataTypes.STRING)
                .add("target_objective", SerializableDataTypes.STRING)
                .add("comparison", ApoliDataTypes.COMPARISON, Comparison.EQUAL),
            CompareScoreCondition::condition
        );
    }

}
