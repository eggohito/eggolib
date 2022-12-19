package io.github.eggohito.eggolib.util;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClassDataRegistryAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.PacketByteBuf;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Environment(EnvType.CLIENT)
public class EggolibMiscUtilClient {

    public static void isInScreen(MinecraftClient minecraftClient, Set<String> screenClassStrings) {

        if (minecraftClient.player == null) return;

        boolean matches = false;
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        Optional<ClassDataRegistry> opt$inGameScreenCDR = ClassDataRegistry.get(ClassUtil.castClass(Screen.class));

        if (opt$inGameScreenCDR.isPresent() && minecraftClient.currentScreen != null) {

            Class<?> currentScreenClass = minecraftClient.currentScreen.getClass();
            ClassDataRegistry<?> inGameScreenCDR = opt$inGameScreenCDR.get();

            if (screenClassStrings.isEmpty()) {
                Collection<Class<?>> inGameScreenClasses = ((ClassDataRegistryAccessor) inGameScreenCDR).getMappings().values();
                matches = inGameScreenClasses.contains(currentScreenClass);
            }

            else matches = screenClassStrings
                .stream()
                .map(inGameScreenCDR::mapStringToClass)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .anyMatch(inGameScreenClass -> inGameScreenClass.isAssignableFrom(currentScreenClass));

        }

        if (Eggolib.PLAYERS_IN_SCREEN.get(minecraftClient.player) != null && Eggolib.PLAYERS_IN_SCREEN.get(minecraftClient.player) == matches) return;
        Eggolib.PLAYERS_IN_SCREEN.put(minecraftClient.player, matches);

        buffer.writeInt(minecraftClient.player.getId());
        buffer.writeBoolean(matches);

        ClientPlayNetworking.send(
            EggolibPackets.IS_IN_SCREEN,
            buffer
        );

    }

    public static void setPerspective(MinecraftClient minecraftClient, EggolibPerspective eggolibPerspective) {

        if (minecraftClient.player == null) return;

        switch (eggolibPerspective) {
            case FIRST_PERSON -> minecraftClient.options.setPerspective(Perspective.FIRST_PERSON);
            case THIRD_PERSON_BACK -> minecraftClient.options.setPerspective(Perspective.THIRD_PERSON_BACK);
            case THIRD_PERSON_FRONT -> minecraftClient.options.setPerspective(Perspective.THIRD_PERSON_FRONT);
        }

    }

    public static void getPerspective(MinecraftClient minecraftClient) {
        getPerspective(minecraftClient, null);
    }

    public static void getPerspective(MinecraftClient minecraftClient, Perspective currentPerspective) {

        if (minecraftClient.player == null) return;

        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        EggolibPerspective currentEggolibPerspective = switch (currentPerspective != null ? currentPerspective : minecraftClient.options.getPerspective()) {
            case FIRST_PERSON -> EggolibPerspective.FIRST_PERSON;
            case THIRD_PERSON_BACK -> EggolibPerspective.THIRD_PERSON_BACK;
            case THIRD_PERSON_FRONT -> EggolibPerspective.THIRD_PERSON_FRONT;
        };

        Eggolib.PLAYERS_PERSPECTIVE.put(minecraftClient.player, currentEggolibPerspective.name());

        buffer.writeInt(minecraftClient.player.getId());
        buffer.writeString(currentEggolibPerspective.name());

        ClientPlayNetworking.send(
            EggolibPackets.GET_PERSPECTIVE,
            buffer
        );

    }

}
