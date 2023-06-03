package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public record EndKeySequencePacket(Map<Identifier, Boolean> powerAndResultMap) implements FabricPacket {

	public static final PacketType<EndKeySequencePacket> TYPE = PacketType.create(
		Eggolib.identifier("end_key_sequence"), EndKeySequencePacket::create
	);

	private static EndKeySequencePacket create(PacketByteBuf buf) {
		return new EndKeySequencePacket(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readBoolean));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powerAndResultMap, PacketByteBuf::writeIdentifier, PacketByteBuf::writeBoolean);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Map.Entry<Identifier, Boolean> powerAndResult : powerAndResultMap.entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerAndResult.getKey());
			if (powerType == null) {
				continue;
			}

			Power power = component.getPower(powerType);
			if (!(power instanceof ActionOnKeySequencePower aoksp)) {
				continue;
			}

			if (powerAndResult.getValue()) {
				aoksp.onSuccess();
			} else {
				aoksp.onFail();
			}

		}

	}

}
