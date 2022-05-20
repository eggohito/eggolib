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

import java.util.*;

public class InScreenCondition {

    private static final String CURRENT_SCREEN_CLASS_NOT_FOUND = "Could not find the screen class for the provided screen class name!";

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;

        if (Eggolib.PLAYERS_CURRENT_SCREEN.isEmpty()) initCurrentScreen(playerEntity);

        String currentScreenClassString = Eggolib.PLAYERS_CURRENT_SCREEN.get(playerEntity);
        if (currentScreenClassString == null || currentScreenClassString.isEmpty()) return false;

        List<String> screenClassStrings = new LinkedList<>();
        Optional<ClassDataRegistry> inGameScreenCDR$temp = ClassDataRegistry.get(ClassUtil.castClass(Screen.class));

        if (data.isPresent("screen")) screenClassStrings.add(data.getString("screen"));
        if (data.isPresent("screens")) screenClassStrings.addAll(data.get("screens"));

        try {

            Class<?> currentScreenClass = Class.forName(currentScreenClassString);
            Eggolib.WARNINGS.remove(CURRENT_SCREEN_CLASS_NOT_FOUND);
            if (inGameScreenCDR$temp.isPresent()) {

                ClassDataRegistry<?> inGameScreenCDR = inGameScreenCDR$temp.get();

                if (!screenClassStrings.isEmpty()) {
                    return screenClassStrings
                        .stream()
                        .map(inGameScreenCDR::mapStringToClass)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .anyMatch(inGameScreenClass -> inGameScreenClass.isAssignableFrom(currentScreenClass));
                }

                else {
                    HashMap<String, Class<?>> inGameScreenClasses = ((ClassDataRegistryAccessor) inGameScreenCDR).getMappings();
                    return inGameScreenClasses.containsValue(currentScreenClass);
                }

            }

            return false;

        }

        catch (ClassNotFoundException e) {
            Eggolib.warnOnce(CURRENT_SCREEN_CLASS_NOT_FOUND);
        }

        return false;

    }

    private static void initCurrentScreen(PlayerEntity playerEntity) {

        if (playerEntity instanceof ClientPlayerEntity clientPlayerEntity) {
            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) clientPlayerEntity).getClient();
            Eggolib.PLAYERS_CURRENT_SCREEN.put(
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
