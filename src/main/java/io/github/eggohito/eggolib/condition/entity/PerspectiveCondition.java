package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;

public class PerspectiveCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;
        if (Eggolib.PLAYERS_CURRENT_PERSPECTIVE.isEmpty()) initCurrentPerspective(playerEntity);

        String currentPerspectiveString = Eggolib.PLAYERS_CURRENT_PERSPECTIVE.get(playerEntity);
        if (currentPerspectiveString == null || currentPerspectiveString.isEmpty()) return false;

        EnumSet<Perspective> perspectives = EnumSet.noneOf(Perspective.class);
        Perspective currentPerspective = Enum.valueOf(Perspective.class, currentPerspectiveString);

        if (data.isPresent("perspective")) perspectives.add(data.get("perspective"));
        if (data.isPresent("perspectives")) perspectives.addAll(data.get("perspectives"));

        return perspectives.isEmpty() || perspectives.contains(currentPerspective);

    }

    private static void initCurrentPerspective(PlayerEntity playerEntity) {

        if (playerEntity instanceof ClientPlayerEntity clientPlayerEntity) {
            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) clientPlayerEntity).getClient();
            Eggolib.PLAYERS_CURRENT_PERSPECTIVE.put(
                clientPlayerEntity,
                minecraftClient.options.getPerspective() == null ? null : minecraftClient.options.getPerspective().name()
            );
        }

        else if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, EggolibPackets.GET_CURRENT_PERSPECTIVE_CLIENT, PacketByteBufs.empty());
        }

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
