package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibEntityComponents;
import net.minecraft.entity.Entity;

import java.util.HashSet;
import java.util.Set;

public class LeaveGroupAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        EggolibEntityComponents.GROUP.maybeGet(entity).ifPresent(
            groupComponent -> {

                Set<String> specifiedGroupIds = new HashSet<>();

                data.ifPresent("group", specifiedGroupIds::add);
                data.ifPresent("groups", specifiedGroupIds::addAll);

                if (specifiedGroupIds.isEmpty()) groupComponent.leaveAllGroups();
                else specifiedGroupIds.forEach(groupComponent::leaveGroup);

                groupComponent.sync();

            }
        );

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("leave_group"),
            new SerializableData()
                .add("group", SerializableDataTypes.STRING, null)
                .add("groups", SerializableDataTypes.STRINGS, null),
            LeaveGroupAction::action
        );
    }

}
