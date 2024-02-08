package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record GetScreenStateS2CPacket() implements FabricPacket {

	public static final PacketType<GetScreenStateS2CPacket> TYPE = PacketType.create(
		Eggolib.identifier("get_screen_state"), GetScreenStateS2CPacket::create
	);

	private static GetScreenStateS2CPacket create(PacketByteBuf buf) {
		return new GetScreenStateS2CPacket();
	}

	@Override
	public void write(PacketByteBuf buf) {

	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
