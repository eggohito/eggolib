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
        ServerPlayNetworking.registerGlobalReceiver(EggolibPackets.GET_CURRENT_SCREEN_SERVER, EggolibPacketsC2S::getCurrentScreen);
    }

    private static void getCurrentScreen(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int entityId = packetByteBuf.readInt();
        boolean isCurrentScreenNull = packetByteBuf.readBoolean();
        String currentScreenClassString = packetByteBuf.readString();

        minecraftServer.execute(
            () -> {
                Entity entity = serverPlayerEntity.getWorld().getEntityById(entityId);
                if (!(entity instanceof PlayerEntity playerEntity)) {
                    Eggolib.LOGGER.warn("[{}] Tried syncing non-PlayerEntity current screen!", Eggolib.MOD_ID);
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

}
