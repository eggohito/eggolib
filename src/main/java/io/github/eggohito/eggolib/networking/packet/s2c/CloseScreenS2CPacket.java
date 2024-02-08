package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record CloseScreenS2CPacket() implements FabricPacket {

	public static final PacketType<CloseScreenS2CPacket> TYPE = PacketType.create(
		Eggolib.identifier("close_screen"), CloseScreenS2CPacket::create
	);

	private static CloseScreenS2CPacket create(PacketByteBuf buf) {
		return new CloseScreenS2CPacket();
	}

	@Override
	public void write(PacketByteBuf buf) {

	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
