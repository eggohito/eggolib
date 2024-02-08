package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.packet.s2c.GetScreenStateS2CPacket;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import io.github.eggohito.eggolib.util.ScreenState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashSet;
import java.util.Set;

public class InScreenCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		if (!(entity instanceof PlayerEntity playerEntity)) {
			return false;
		}
		if (!Eggolib.PLAYERS_SCREEN.containsKey(playerEntity)) {
			initializeScreen(playerEntity);
		}

		Set<String> screenClassNames = new HashSet<>();
		ScreenState screenState = Eggolib.PLAYERS_SCREEN.get(playerEntity);
		if (screenState == null) {
			return false;
		}

		data.ifPresent("screen", screenClassNames::add);
		data.ifPresent("screens", screenClassNames::addAll);

		return screenState.inAnyOr(screenClassNames);

	}

	public static void initializeScreen(PlayerEntity playerEntity) {

		if (playerEntity.getWorld().isClient) {
			MinecraftClient client = ((ClientPlayerEntityAccessor) playerEntity).getClient();
			client.execute(
				() -> MiscUtilClient.getScreenState(client)
			);
		} else {
			ServerPlayNetworking.send(
				(ServerPlayerEntity) playerEntity,
				new GetScreenStateS2CPacket()
			);
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
