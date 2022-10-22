package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.EggolibAbstractTeam;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class InTeamCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        Set<EggolibAbstractTeam> specifiedAbstractTeams = new HashSet<>();

        data.ifPresent("team", specifiedAbstractTeams::add);
        data.ifPresent("teams", specifiedAbstractTeams::addAll);

        if (specifiedAbstractTeams.isEmpty()) return entity.getScoreboardTeam() != null;
        else return specifiedAbstractTeams.stream().anyMatch(eggolibAbstractTeam -> eggolibAbstractTeam.isEqualTo(entity.getScoreboardTeam()));

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_team"),
            new SerializableData()
                .add("team", EggolibDataTypes.BACKWARDS_COMPATIBLE_ABSTRACT_TEAM, null)
                .add("teams", EggolibDataTypes.BACKWARDS_COMPATIBLE_ABSTRACT_TEAMS, null),
            InTeamCondition::condition
        );
    }

}
