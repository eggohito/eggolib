/*
    MIT License

    Copyright (c) 2021 apace100

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY
    , FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
    OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.
 */

package io.github.eggohito.eggolib.networking;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EggolibMiscUtilClient;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class EggolibPacketsS2C {

    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(EggolibPackets.HANDSHAKE, EggolibPacketsS2C::handleHandshake);
        ClientPlayConnectionEvents.INIT.register(
            (clientPlayNetworkHandler, minecraftClient) -> {
                ClientPlayNetworking.registerReceiver(EggolibPackets.CLOSE_SCREEN, EggolibPacketsS2C::closeScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.IS_IN_SCREEN, EggolibPacketsS2C::isInScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.SET_PERSPECTIVE, EggolibPacketsS2C::setPerspective);
                ClientPlayNetworking.registerReceiver(EggolibPackets.GET_PERSPECTIVE, EggolibPacketsS2C::getPerspective);
            }
        );
    }

    private static CompletableFuture<PacketByteBuf> handleHandshake(MinecraftClient minecraftClient, ClientLoginNetworkHandler clientLoginNetworkHandler, PacketByteBuf packetByteBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> genericFutureListenerConsumer) {

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(Eggolib.semanticVersion.length);

        for (int i = 0; i < Eggolib.semanticVersion.length; i++) {
            buffer.writeInt(Eggolib.semanticVersion[i]);
        }

        return CompletableFuture.completedFuture(buffer);

    }

    private static void closeScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(
            () -> minecraftClient.setScreen(null)
        );
    }

    private static void isInScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int j = packetByteBuf.readInt();
        Set<String> screenClassStrings = new HashSet<>();
        for (int i = 0; i < j; i++) {
            screenClassStrings.add(packetByteBuf.readString());
        }

        minecraftClient.execute(
            () -> EggolibMiscUtilClient.isInScreen(minecraftClient, screenClassStrings)
        );

    }

    private static void setPerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        String eggolibPerspectiveString = packetByteBuf.readString();
        EggolibPerspective eggolibPerspective = Enum.valueOf(EggolibPerspective.class, eggolibPerspectiveString);

        minecraftClient.execute(
            () -> EggolibMiscUtilClient.setPerspective(minecraftClient, eggolibPerspective)
        );

    }

    private static void getPerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        minecraftClient.execute(
            () -> EggolibMiscUtilClient.getPerspective(minecraftClient)
        );

    }

}
