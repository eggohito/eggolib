package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
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

        if (!(entity instanceof PlayerEntity playerEntity)) return;

        EggolibPerspective eggolibPerspective = data.get("perspective");

        if (playerEntity.world.isClient) {

            MinecraftClient minecraftClient = ((ClientPlayerEntityAccessor) playerEntity).getClient();
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

        else {

            PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
            buffer.writeString(eggolibPerspective.toString());

            ServerPlayNetworking.send(
                (ServerPlayerEntity) playerEntity,
                EggolibPackets.Client.SET_PERSPECTIVE,
                buffer
            );

        }

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
