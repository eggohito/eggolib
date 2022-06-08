package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

public class EggolibPacketsC2S {

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.CHECK_SCREEN_SERVER, EggolibPacketsC2S::checkScreenServer);
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.CHECK_PERSPECTIVE_SERVER, EggolibPacketsC2S::checkPerspectiveServer);
    }

    private static void checkScreenServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        boolean matches = packetByteBuf.readBoolean();

        minecraftServer.execute(
            () -> {

                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) return;

                Eggolib.PLAYERS_IN_SCREEN.put(playerEntity, matches);

            }

        );

    }

    private static void checkPerspectiveServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        String eggolibPerspectiveString = packetByteBuf.readString();

        minecraftServer.execute(
            () -> {

                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) return;

                Eggolib.PLAYERS_PERSPECTIVE.put(playerEntity, eggolibPerspectiveString);

            }
        );

    }

}
