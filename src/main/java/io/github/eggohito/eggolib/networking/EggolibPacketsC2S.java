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
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.SEND_CURRENT_SCREEN_SERVER, EggolibPacketsC2S::sendCurrentScreen);
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.SEND_CURRENT_PERSPECTIVE_SERVER, EggolibPacketsC2S::sendCurrentPerspective);
    }

    private static void sendCurrentScreen(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        boolean isCurrentScreenNull = packetByteBuf.readBoolean();
        String currentScreenClassString = packetByteBuf.readString();

        minecraftServer.execute(
            () -> {
                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) {
                    Eggolib.LOGGER.warn("[{}] Tried getting the current screen of a non-PlayerEntity!", Eggolib.MOD_ID);
                    return;
                };
                if (isCurrentScreenNull) {
                    Eggolib.playerCurrentScreenHashMap.put(playerEntity, null);
                }
                else {
                    Eggolib.playerCurrentScreenHashMap.put(playerEntity, currentScreenClassString);
                }
            }
        );
    }

    private static void sendCurrentPerspective(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        String perspectiveString = packetByteBuf.readString();

        minecraftServer.execute(
            () -> {
                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) {
                    Eggolib.LOGGER.warn("[{}] Tried getting the current perspective of a non-PlayerEntity!", Eggolib.MOD_ID);
                    return;
                }
                Eggolib.playerCurrentPerspectiveHashMap.put(playerEntity, perspectiveString);
            }
        );

    }

}
