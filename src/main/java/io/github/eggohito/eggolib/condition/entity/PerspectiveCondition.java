package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.EnumSet;

public class PerspectiveCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity)) return false;

        int matches = 0;
        Perspective currentPerspective = MinecraftClient.getInstance().options.getPerspective();

        if (data.isPresent("perspective") || data.isPresent("perspectives")) {

            if (data.isPresent("perspective")) {

                Perspective perspective = data.get("perspective");
                if (perspective == currentPerspective) matches++;
            }

            if (data.isPresent("perspectives")) {

                EnumSet<Perspective> perspectives = data.get("perspectives");
                if (perspectives.contains(currentPerspective)) matches++;
            }
        }

        else return true;

        return (matches > 0);
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("perspective"),
            new SerializableData()
                .add("perspective", EggolibDataTypes.PERSPECTIVE, null)
                .add("perspectives", EggolibDataTypes.PERSPECTIVE_SET, null),
            PerspectiveCondition::condition
        );
    }
}
