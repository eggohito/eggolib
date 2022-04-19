package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.github.eggohito.eggolib.networking.EggolibPacketsS2C;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;

public class EggolibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EggolibPacketsS2C.register();
        EggolibClassDataClient.registerAll();

        ClientTickEvents.START_CLIENT_TICK.register(
            minecraftClient -> {
                getPlayerCurrentScreen(minecraftClient);
                getPlayerCurrentPerspective(minecraftClient);
            }
        );

    }

    private static void getPlayerCurrentScreen(MinecraftClient minecraftClient) {

        if (minecraftClient.player != null) {
            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
            buffer.writeInt(minecraftClient.player.getId());
            if (minecraftClient.currentScreen == null) {
                buffer.writeBoolean(true);
                buffer.writeString("");
            }
            else {
                buffer.writeBoolean(false);
                buffer.writeString(minecraftClient.currentScreen.getClass().getName());
            }
            ClientPlayNetworking.send(EggolibPackets.GET_CURRENT_SCREEN_SERVER, buffer);
        }

    }

    private static void getPlayerCurrentPerspective(MinecraftClient minecraftClient) {

        if (minecraftClient.player != null) {
            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
            buffer.writeInt(minecraftClient.player.getId());
            buffer.writeString(minecraftClient.options.getPerspective().name());
            ClientPlayNetworking.send(EggolibPackets.GET_PERSPECTIVE_SERVER, buffer);
        }

    }

}
