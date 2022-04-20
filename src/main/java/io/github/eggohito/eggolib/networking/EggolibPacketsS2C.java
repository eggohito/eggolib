package io.github.eggohito.eggolib.networking;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.PacketByteBuf;

public class EggolibPacketsS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayConnectionEvents.INIT.register(
            (clientPlayNetworkHandler, minecraftClient) -> {
                ClientPlayNetworking.registerReceiver(EggolibPackets.CLOSE_SCREEN_CLIENT, EggolibPacketsS2C::closeScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.SET_PERSPECTIVE_CLIENT, EggolibPacketsS2C::setPerspective);
                ClientPlayNetworking.registerReceiver(EggolibPackets.GET_CURRENT_SCREEN_CLIENT, EggolibPacketsS2C::getCurrentScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.GET_CURRENT_PERSPECTIVE_CLIENT, EggolibPacketsS2C::getCurrentPerspective);
            }
        );
    }

    private static void closeScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(
            () -> minecraftClient.setScreen(null)
        );
    }

    private static void setPerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        String enumName = packetByteBuf.readString();
        Perspective perspective = Enum.valueOf(Perspective.class, enumName);
        minecraftClient.execute(
            () -> minecraftClient.options.setPerspective(perspective)
        );
    }

    private static void getCurrentScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(
            () -> {
                if (minecraftClient.player == null) return;

                PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                buffer.writeInt(minecraftClient.player.getId());
                if (minecraftClient.currentScreen == null) buffer.writeString("");
                else buffer.writeString(minecraftClient.currentScreen.getClass().getName());

                ClientPlayNetworking.send(EggolibPackets.SYNC_CURRENT_SCREEN_SERVER, buffer);
            }
        );
    }

    private static void getCurrentPerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(
            () -> {
                if (minecraftClient.player == null) return;

                PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                buffer.writeInt(minecraftClient.player.getId());
                buffer.writeString(minecraftClient.options.getPerspective().name());

                ClientPlayNetworking.send(EggolibPackets.SYNC_CURRENT_PERSPECTIVE_SERVER, buffer);
            }
        );
    }

}
