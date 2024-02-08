package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.packet.s2c.SetPerspectiveS2CPacket;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class SetPerspectiveAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		if (!(entity instanceof PlayerEntity playerEntity)) {
			return;
		}

		EggolibPerspective eggolibPerspective = data.get("perspective");
		if (playerEntity.getWorld().isClient) {
			MinecraftClient client = ((ClientPlayerEntityAccessor) playerEntity).getClient();
			client.execute(() -> MiscUtilClient.setPerspective(client, eggolibPerspective));
		} else {
			ServerPlayNetworking.send(
				(ServerPlayerEntity) playerEntity,
				new SetPerspectiveS2CPacket(eggolibPerspective)
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
