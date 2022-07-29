package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.entity.MiscComponent;
import net.minecraft.entity.Entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class HasTagCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        Optional<MiscComponent> miscComponent = MiscComponent.KEY.maybeGet(entity);
        if (miscComponent.isEmpty()) return false;

        Set<String> specifiedScoreboardTags = new HashSet<>();
        Set<String> scoreboardTags = miscComponent.get().getScoreboardTags();

        data.ifPresent("tag", specifiedScoreboardTags::add);
        data.ifPresent("tags", specifiedScoreboardTags::addAll);

        if (specifiedScoreboardTags.isEmpty()) return !scoreboardTags.isEmpty();
        else return !Collections.disjoint(scoreboardTags, specifiedScoreboardTags);

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("has_tag"),
            new SerializableData()
                .add("tag", SerializableDataTypes.STRING, null)
                .add("tags", SerializableDataTypes.STRINGS, null),
            HasTagCondition::condition
        );
    }

}
