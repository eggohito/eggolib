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
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InScreenCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof PlayerEntity playerEntity)) return false;

        boolean matches = false;
        Set<String> screenClassStrings = new HashSet<>();
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

        if (data.isPresent("screen")) screenClassStrings.add(data.get("screen"));
        if (data.isPresent("screens")) screenClassStrings.addAll(data.get("screens"));


        if (entity.world.isClient) {

            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) playerEntity).getClient();
            Optional<ClassDataRegistry> opt$inGameScreenCDR = ClassDataRegistry.get(ClassUtil.castClass(Screen.class));

            if (minecraftClient.player == null) return false;

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

        else {

            buffer.writeInt(screenClassStrings.size());
            if (screenClassStrings.size() > 0) {
                for (int i = 0; i < screenClassStrings.size(); i++) {
                    buffer.writeString(screenClassStrings.stream().toList().get(i));
                }
            }

            ServerPlayNetworking.send(
                (ServerPlayerEntity) playerEntity,
                EggolibPackets.Client.GET_SCREEN,
                buffer
            );

        }

        if (Eggolib.PLAYERS_IN_SCREEN.get(playerEntity) == null) return false;
        else return Eggolib.PLAYERS_IN_SCREEN.get(playerEntity);

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
