package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;

public class PerspectiveCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;
        if (!Eggolib.PLAYERS_PERSPECTIVE.containsKey(playerEntity)) initializePerspective(playerEntity);

        String currentEggolibPerspectiveString = Eggolib.PLAYERS_PERSPECTIVE.get(playerEntity);
        if (currentEggolibPerspectiveString == null || currentEggolibPerspectiveString.isEmpty()) return false;

        EnumSet<EggolibPerspective> eggolibPerspectives = EnumSet.noneOf(EggolibPerspective.class);
        EggolibPerspective currentEggolibPerspective = Enum.valueOf(EggolibPerspective.class, currentEggolibPerspectiveString);

        if (data.isPresent("perspective")) eggolibPerspectives.add(data.get("perspective"));
        if (data.isPresent("perspectives")) eggolibPerspectives.addAll(data.get("perspectives"));

        return !eggolibPerspectives.isEmpty() && eggolibPerspectives.contains(currentEggolibPerspective);

    }

    private static void initializePerspective(PlayerEntity playerEntity) {

        if (playerEntity.world.isClient) {
            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) playerEntity).getClient();
            minecraftClient.execute(
                () -> MiscUtilClient.getPerspective(minecraftClient)
            );
        }

        else ServerPlayNetworking.send(
            (ServerPlayerEntity) playerEntity,
            EggolibPackets.GET_PERSPECTIVE,
            PacketByteBufs.empty()
        );

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
