package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class InScreenCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;
        if (!Eggolib.PLAYERS_SCREEN.containsKey(playerEntity)) initializeScreen(playerEntity);

        Set<String> screenClassNames = new HashSet<>();
        String currentScreenClassName = Eggolib.PLAYERS_SCREEN.get(playerEntity);

        data.ifPresent("screen", screenClassNames::add);
        data.ifPresent("screens", screenClassNames::addAll);

        if (screenClassNames.isEmpty()) return currentScreenClassName != null;
        else return screenClassNames.contains(currentScreenClassName);

    }

    public static void initializeScreen(PlayerEntity playerEntity) {

        if (playerEntity.world.isClient) {
            MinecraftClient client = ((ClientPlayerEntityAccessor) playerEntity).getClient();
            client.execute(
                () -> MiscUtilClient.syncScreen(client)
            );
        }

        else ServerPlayNetworking.send(
            (ServerPlayerEntity) playerEntity,
            EggolibPackets.SYNC_SCREEN,
            PacketByteBufs.empty()
        );

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_screen"),
            new SerializableData()
                .add("screen", SerializableDataTypes.STRING, null)
                .add("screens", SerializableDataTypes.STRINGS, null),
            InScreenCondition::condition
        );
    }

}
