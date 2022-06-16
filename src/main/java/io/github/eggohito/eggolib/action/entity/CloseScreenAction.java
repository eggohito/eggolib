package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class CloseScreenAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return;

        if (playerEntity.world.isClient) {
            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) playerEntity).getClient();
            minecraftClient.execute(
                () -> minecraftClient.setScreen(null)
            );
        }

        else ServerPlayNetworking.send(
            (ServerPlayerEntity) playerEntity,
            EggolibPackets.CLOSE_SCREEN_CLIENT,
            PacketByteBufs.empty()
        );

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("close_screen"),
            new SerializableData(),
            CloseScreenAction::action
        );
    }
}
