package io.github.eggohito.eggolib.networking.packet;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record VersionHandshakePacket(int[] semver) implements FabricPacket {

	public static final PacketType<VersionHandshakePacket> TYPE = PacketType.create(
		Eggolib.identifier("version_handshake"), VersionHandshakePacket::read
	);

	private static VersionHandshakePacket read(PacketByteBuf buf) {
		return new VersionHandshakePacket(buf.readIntArray());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeIntArray(semver);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
