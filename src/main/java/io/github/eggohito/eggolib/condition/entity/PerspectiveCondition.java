package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;

public class PerspectiveCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;

        ServerPlayNetworking.send((ServerPlayerEntity) playerEntity, EggolibPackets.GET_CURRENT_PERSPECTIVE_CLIENT, PacketByteBufs.empty());

        int matches = 0;
        String perspectiveString = Eggolib.playerCurrentPerspectiveHashMap.get(playerEntity);
        if (perspectiveString == null) return false;

        Perspective perspectiveEnum = Enum.valueOf(Perspective.class, perspectiveString);

        if (data.isPresent("perspective") || data.isPresent("perspectives")) {

            if (data.isPresent("perspective")) {

                Perspective perspective = data.get("perspective");
                if (perspective == perspectiveEnum) matches++;
            }

            if (data.isPresent("perspectives")) {

                EnumSet<Perspective> perspectives = data.get("perspectives");
                if (perspectives.contains(perspectiveEnum)) matches++;
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
