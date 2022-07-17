package io.github.eggohito.eggolib.condition.bientity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibEntityComponents;
import io.github.eggohito.eggolib.component.entity.GroupComponent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class InSameGroupCondition {

    public static boolean condition(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

        Optional<GroupComponent> actorGroupComponent = EggolibEntityComponents.GROUP.maybeGet(actorAndTarget.getLeft());
        Optional<GroupComponent> targetGroupComponent = EggolibEntityComponents.GROUP.maybeGet(actorAndTarget.getRight());

        if (actorGroupComponent.isEmpty() || targetGroupComponent.isEmpty()) return false;

        Set<String> actorGroups = actorGroupComponent.get().getGroups();
        Set<String> targetGroups = targetGroupComponent.get().getGroups();

        return !Collections.disjoint(actorGroups, targetGroups);

    }

    public static ConditionFactory<Pair<Entity, Entity>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_same_group"),
            new SerializableData(),
            InSameGroupCondition::condition
        );
    }

}
