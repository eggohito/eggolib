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

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClassDataRegistryAccessor;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import io.netty.buffer.Unpooled;
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
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class EggolibPacketsS2C {

    public static void register() {
        ClientLoginNetworking.registerGlobalReceiver(EggolibPackets.HANDSHAKE, EggolibPacketsS2C::handleHandshake);
        ClientPlayConnectionEvents.INIT.register(
            (clientPlayNetworkHandler, minecraftClient) -> {
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.SET_SCREEN, EggolibPacketsS2C::setScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.SET_PERSPECTIVE, EggolibPacketsS2C::setPerspective);
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.GET_SCREEN, EggolibPacketsS2C::getScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.GET_PERSPECTIVE, EggolibPacketsS2C::getPerspective);
            }
        );
    }

    private static CompletableFuture<PacketByteBuf> handleHandshake(MinecraftClient minecraftClient, ClientLoginNetworkHandler clientLoginNetworkHandler, PacketByteBuf packetByteBuf, Consumer<GenericFutureListener<? extends Future<? super Void>>> genericFutureListenerConsumer) {

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(Eggolib.SEMVER.length);

        for (int i = 0; i < Eggolib.SEMVER.length; i++) {
            buffer.writeInt(Eggolib.SEMVER[i]);
        }

        return CompletableFuture.completedFuture(buffer);

    }

    private static void setScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(
            () -> minecraftClient.setScreen(null)
        );
    }

    private static void setPerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        String eggolibPerspectiveString = packetByteBuf.readString();
        EggolibPerspective eggolibPerspective = Enum.valueOf(EggolibPerspective.class, eggolibPerspectiveString);

        minecraftClient.execute(
            () -> {

                if (minecraftClient.player == null) return;

                switch (eggolibPerspective) {
                    case FIRST_PERSON:
                        minecraftClient.options.setPerspective(Perspective.FIRST_PERSON);
                        break;
                    case THIRD_PERSON_BACK:
                        minecraftClient.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                        break;
                    case THIRD_PERSON_FRONT:
                        minecraftClient.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
                        break;
                }

            }
        );

    }

    private static void getScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        int j = packetByteBuf.readInt();
        Set<String> screenClassStrings = new HashSet<>();
        for (int i = 0; i < j; i++) {
            screenClassStrings.add(packetByteBuf.readString());
        }

        minecraftClient.execute(
            () -> {

                boolean matches = false;
                PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                Optional<ClassDataRegistry> opt$inGameScreenCDR = ClassDataRegistry.get(ClassUtil.castClass(Screen.class));

                if (minecraftClient.player == null) return;

                if (opt$inGameScreenCDR.isPresent()) {

                    ClassDataRegistry<?> inGameScreenCDR = opt$inGameScreenCDR.get();

                    if (minecraftClient.currentScreen != null) {

                        if (screenClassStrings.isEmpty()) {
                            HashMap<String, Class<?>> inGameScreenClasses = ((ClassDataRegistryAccessor) inGameScreenCDR).getMappings();
                            matches = inGameScreenClasses.containsValue(minecraftClient.currentScreen.getClass());
                        }

                        else matches = screenClassStrings
                            .stream()
                            .map(inGameScreenCDR::mapStringToClass)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .anyMatch(inGameScreenClass -> inGameScreenClass.isAssignableFrom(minecraftClient.currentScreen.getClass()));


                    }

                }

                buffer.writeInt(minecraftClient.player.getId());
                buffer.writeBoolean(matches);

                ClientPlayNetworking.send(
                    EggolibPackets.Server.GET_SCREEN,
                    buffer
                );

            }
        );

    }

    private static void getPerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

        minecraftClient.execute(
            () -> {

                if (minecraftClient.player == null) return;

                EggolibPerspective eggolibPerspective = null;
                PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

                switch (minecraftClient.options.getPerspective()) {
                    case FIRST_PERSON:
                        eggolibPerspective = EggolibPerspective.FIRST_PERSON;
                        break;
                    case THIRD_PERSON_BACK:
                        eggolibPerspective = EggolibPerspective.THIRD_PERSON_BACK;
                        break;
                    case THIRD_PERSON_FRONT:
                        eggolibPerspective = EggolibPerspective.THIRD_PERSON_FRONT;
                        break;
                }

                buffer.writeInt(minecraftClient.player.getId());
                buffer.writeString(eggolibPerspective.toString());

                ClientPlayNetworking.send(EggolibPackets.Server.GET_PERSPECTIVE, buffer);

            }
        );

    }

}
