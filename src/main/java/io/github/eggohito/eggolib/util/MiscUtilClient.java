package io.github.eggohito.eggolib.util;

import com.google.common.collect.HashBiMap;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.apace100.calio.ClassDataRegistryAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.PacketByteBuf;

import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class MiscUtilClient {

    public static void syncScreen(MinecraftClient client) {
        syncScreen(client, client.currentScreen);
    }

    @SuppressWarnings("rawtypes")
    public static void syncScreen(MinecraftClient client, Screen screen) {

        if (client.player == null) return;

        String screenClassName = null;
        Optional<ClassDataRegistry> opt$inGameScreenCDR = ClassDataRegistry.get(Screen.class);

        if (opt$inGameScreenCDR.isPresent() && screen != null) {

            ClassDataRegistry<?> inGameScreenCDR = opt$inGameScreenCDR.get();
            Class<?> screenClass = screen.getClass();

            HashBiMap<String, Class<?>> inGameScreens = HashBiMap.create(((ClassDataRegistryAccessor) inGameScreenCDR).getMappings());
            if (inGameScreens.containsValue(screenClass)) screenClassName = inGameScreens.inverse().get(screenClass);

        }

        if (Eggolib.PLAYERS_SCREEN.containsKey(client.player) && Objects.equals(Eggolib.PLAYERS_SCREEN.get(client.player), screenClassName)) return;
        Eggolib.PLAYERS_SCREEN.put(client.player, screenClassName);

        PacketByteBuf buffer = PacketByteBufs.create();
        buffer.writeInt(client.player.getId());
        buffer.writeString(screenClassName);

        ClientPlayNetworking.send(
            EggolibPackets.SYNC_SCREEN,
            buffer
        );

        Eggolib.LOGGER.warn("Current screen: " + screenClassName);

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
        getPerspective(minecraftClient, minecraftClient.options.getPerspective());
    }

    public static void getPerspective(MinecraftClient minecraftClient, Perspective currentPerspective) {

        if (minecraftClient.player == null) return;

        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        String currentEggolibPerspective = switch (currentPerspective) {
            case FIRST_PERSON -> EggolibPerspective.FIRST_PERSON.toString();
            case THIRD_PERSON_BACK -> EggolibPerspective.THIRD_PERSON_BACK.toString();
            case THIRD_PERSON_FRONT -> EggolibPerspective.THIRD_PERSON_FRONT.toString();
        };

        if (Eggolib.PLAYERS_PERSPECTIVE.get(minecraftClient.player) != null && Eggolib.PLAYERS_PERSPECTIVE.get(minecraftClient.player).equalsIgnoreCase(currentEggolibPerspective)) return;
        Eggolib.PLAYERS_PERSPECTIVE.put(minecraftClient.player, currentEggolibPerspective);

        buffer.writeInt(minecraftClient.player.getId());
        buffer.writeString(currentEggolibPerspective);

        ClientPlayNetworking.send(
            EggolibPackets.GET_PERSPECTIVE,
            buffer
        );

    }

}
