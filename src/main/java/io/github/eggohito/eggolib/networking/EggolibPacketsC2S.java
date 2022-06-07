package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class EggolibPacketsC2S {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.SYNC_CURRENT_SCREEN_SERVER, EggolibPacketsC2S::syncCurrentScreen);
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.SYNC_CURRENT_PERSPECTIVE_SERVER, EggolibPacketsC2S::syncCurrentPerspective);
    }

    private static void syncCurrentScreen(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        String currentScreenClassString = packetByteBuf.readString();

        minecraftServer.execute(
            () -> {

                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) return;

                Eggolib.PLAYERS_CURRENT_SCREEN.put(playerEntity, currentScreenClassString);

            }

        );

    }

    private static void syncCurrentPerspective(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        String eggolibPerspectiveString = packetByteBuf.readString();

        minecraftServer.execute(
            () -> {

                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) return;

                Eggolib.PLAYERS_CURRENT_PERSPECTIVE.put(playerEntity, eggolibPerspectiveString);

            }
        );

    }

}
