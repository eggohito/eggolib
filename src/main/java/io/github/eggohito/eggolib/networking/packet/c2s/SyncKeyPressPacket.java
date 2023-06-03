package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.util.Key;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SyncKeyPressPacket(Map<Identifier, String> powerAndKeyMap) implements FabricPacket {

	public static final PacketType<SyncKeyPressPacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_key_press"), SyncKeyPressPacket::create
	);

	private static SyncKeyPressPacket create(PacketByteBuf buf) {
		return new SyncKeyPressPacket(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readString));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powerAndKeyMap, PacketByteBuf::writeIdentifier, PacketByteBuf::writeString);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Map.Entry<Identifier, String> powerAndKey : powerAndKeyMap.entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerAndKey.getKey());
			if (powerType == null) {
				continue;
			}

			Power power = component.getPower(powerType);
			if (power instanceof ActionOnKeySequencePower aoksp) {
				aoksp.addKeyToSequence(new Key(powerAndKey.getValue()));
			}

		}

	}

}
