package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetPerspectiveAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof ServerPlayerEntity serverPlayerEntity)) return;

        EggolibPerspective eggolibPerspective = data.get("perspective");
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());

        buffer.writeString(eggolibPerspective.toString());

        ServerPlayNetworking.send(serverPlayerEntity, EggolibPackets.SET_PERSPECTIVE_CLIENT, buffer);

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("set_perspective"),
            new SerializableData()
                .add("perspective", EggolibDataTypes.PERSPECTIVE),
            SetPerspectiveAction::action
        );
    }
}
