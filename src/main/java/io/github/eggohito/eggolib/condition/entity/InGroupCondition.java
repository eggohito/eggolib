package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibEntityComponents;
import io.github.eggohito.eggolib.component.entity.GroupComponent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InGroupCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        boolean isInGroup = false;
        Optional<GroupComponent> opt$groupComponent = EggolibEntityComponents.GROUP.maybeGet(entity);

        if (opt$groupComponent.isPresent()) {

            Set<String> specifiedGroupIds = new HashSet<>();
            GroupComponent groupComponent = opt$groupComponent.get();

            data.ifPresent("group", specifiedGroupIds::add);
            data.ifPresent("groups", specifiedGroupIds::addAll);

            if (specifiedGroupIds.isEmpty()) isInGroup = !groupComponent.getGroups().isEmpty();
            else isInGroup = !Collections.disjoint(groupComponent.getGroups(), specifiedGroupIds);

        }

        return isInGroup;

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_group"),
            new SerializableData()
                .add("group", SerializableDataTypes.STRING, null)
                .add("groups", SerializableDataTypes.STRINGS, null),
            InGroupCondition::condition
        );
    }

}
