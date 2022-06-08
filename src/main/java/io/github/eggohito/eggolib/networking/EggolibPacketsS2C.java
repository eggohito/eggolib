package io.github.eggohito.eggolib.networking;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClassDataRegistryAccessor;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.PacketByteBuf;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EggolibPacketsS2C {

    @Environment(EnvType.CLIENT)
    public static void register() {
        ClientPlayConnectionEvents.INIT.register(
            (clientPlayNetworkHandler, minecraftClient) -> {
                ClientPlayNetworking.registerReceiver(EggolibPackets.CLOSE_SCREEN_CLIENT, EggolibPacketsS2C::closeScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.CHANGE_PERSPECTIVE_CLIENT, EggolibPacketsS2C::changePerspective);
                ClientPlayNetworking.registerReceiver(EggolibPackets.CHECK_SCREEN_CLIENT, EggolibPacketsS2C::checkScreenClient);
                ClientPlayNetworking.registerReceiver(EggolibPackets.CHECK_PERSPECTIVE_CLIENT, EggolibPacketsS2C::checkPerspectiveClient);
            }
        );
    }

    private static void closeScreen(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        minecraftClient.execute(
            () -> minecraftClient.setScreen(null)
        );
    }

    private static void changePerspective(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

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

    private static void checkScreenClient(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

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
                    EggolibPackets.CHECK_SCREEN_SERVER,
                    buffer
                );

            }
        );

    }

    private static void checkPerspectiveClient(MinecraftClient minecraftClient, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {

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

                ClientPlayNetworking.send(EggolibPackets.CHECK_PERSPECTIVE_SERVER, buffer);

            }
        );

    }

}
