package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public record EndKeySequenceC2SPacket(Map<Identifier, Boolean> powerAndResultMap) implements FabricPacket {

	public static final PacketType<EndKeySequenceC2SPacket> TYPE = PacketType.create(
		Eggolib.identifier("end_key_sequence"), EndKeySequenceC2SPacket::create
	);

	private static EndKeySequenceC2SPacket create(PacketByteBuf buf) {
		return new EndKeySequenceC2SPacket(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readBoolean));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powerAndResultMap, PacketByteBuf::writeIdentifier, PacketByteBuf::writeBoolean);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
