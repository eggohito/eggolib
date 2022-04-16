package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class SetPerspectiveAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity)) return;

        MinecraftClient.getInstance().execute(
            () -> MinecraftClient.getInstance().options.setPerspective(data.get("perspective"))
        );
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("set_perspective"),
            new SerializableData()
                .add("perspective", EggolibDataTypes.PERSPECTIVE),
            SetPerspectiveAction::action
        );
    }
}
