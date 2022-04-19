package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
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
                ClientPlayNetworking.registerReceiver(EggolibPackets.CLOSE_GUI_CLIENT, EggolibPacketsS2C::closeGui);
                ClientPlayNetworking.registerReceiver(EggolibPackets.SET_PERSPECTIVE_CLIENT, EggolibPacketsS2C::setPerspective);
            }
        );
    }

    private static void closeGui(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
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

}
