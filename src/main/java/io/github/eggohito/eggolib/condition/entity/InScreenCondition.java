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
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class InScreenCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;

        Set<String> screenClassStrings = new HashSet<>();
        PacketByteBuf buffer = PacketByteBufs.create();

        data.ifPresent("screen", screenClassStrings::add);
        data.ifPresent("screens", screenClassStrings::addAll);

        if (entity.world.isClient) {
            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) playerEntity).getClient();
            minecraftClient.execute(
                () -> MiscUtilClient.isInScreen(minecraftClient, screenClassStrings)
            );
        }

        else {

            buffer.writeInt(screenClassStrings.size());
            for (String screenClassString : screenClassStrings) {
                buffer.writeString(screenClassString);
            }

            ServerPlayNetworking.send(
                (ServerPlayerEntity) playerEntity,
                EggolibPackets.IS_IN_SCREEN,
                buffer
            );

        }

        return Eggolib.PLAYERS_IN_SCREEN.getOrDefault(playerEntity, false);

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
