package io.github.eggohito.eggolib.networking;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
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

@Environment(EnvType.CLIENT)
public class EggolibPacketsS2C {

    public static void register() {
        ClientPlayConnectionEvents.INIT.register(
            (clientPlayNetworkHandler, minecraftClient) -> {
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.SET_SCREEN, EggolibPacketsS2C::setScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.SET_PERSPECTIVE, EggolibPacketsS2C::setPerspective);
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.GET_SCREEN, EggolibPacketsS2C::getScreen);
                ClientPlayNetworking.registerReceiver(EggolibPackets.Client.GET_PERSPECTIVE, EggolibPacketsS2C::getPerspective);
            }
        );
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
