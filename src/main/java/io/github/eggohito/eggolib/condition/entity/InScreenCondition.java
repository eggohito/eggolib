package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClassDataRegistryAccessor;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class InScreenCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;

        if (Eggolib.playerCurrentScreenHashMap.isEmpty()) initCurrentScreen(playerEntity);

        String currentScreenClassString = Eggolib.playerCurrentScreenHashMap.get(playerEntity);
        if (currentScreenClassString == null || currentScreenClassString.isEmpty()) return false;

        List<String> screenClassStrings = new LinkedList<>();
        Optional<ClassDataRegistry> optionalInGameScreen = ClassDataRegistry.get(ClassUtil.castClass(Screen.class));

        if (data.isPresent("screen")) screenClassStrings.add(data.getString("screen"));
        if (data.isPresent("screens")) screenClassStrings.addAll(data.get("screens"));

        try {

            Class<?> currentScreenClass = Class.forName(currentScreenClassString);
            if (optionalInGameScreen.isPresent()) {

                ClassDataRegistry<?> inGameScreen = optionalInGameScreen.get();

                if (!screenClassStrings.isEmpty()) {
                    return screenClassStrings
                        .stream()
                        .map(inGameScreen::mapStringToClass)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .anyMatch(classString -> classString.isAssignableFrom(currentScreenClass));
                }

                else {
                    HashMap<String, Class<?>> inGameScreenMappings = ((ClassDataRegistryAccessor) inGameScreen).getMappings();
                    return inGameScreenMappings.containsValue(currentScreenClass);
                }

            }

            return false;

        }

        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;

    }

    private static void initCurrentScreen(PlayerEntity playerEntity) {

        if (playerEntity instanceof ClientPlayerEntity clientPlayerEntity) {
            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) clientPlayerEntity).getClient();
            Eggolib.playerCurrentScreenHashMap.put(
                playerEntity,
                minecraftClient.currentScreen == null ? null : minecraftClient.currentScreen.getClass().getName()
            );
        }

        else if (playerEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, EggolibPackets.GET_CURRENT_SCREEN_CLIENT, PacketByteBufs.empty());
        }

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_screen"),
            new SerializableData()
                .add("screen", SerializableDataTypes.STRING, null)
                .add("screens", SerializableDataTypes.STRINGS, null),
            InScreenCondition::condition
        );
    }
}
