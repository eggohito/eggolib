package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record SyncPerspectiveC2SPacket(String perspective) implements FabricPacket {

	public static final PacketType<SyncPerspectiveC2SPacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_perspective"), SyncPerspectiveC2SPacket::create
	);

	private static SyncPerspectiveC2SPacket create(PacketByteBuf buf) {
		return new SyncPerspectiveC2SPacket(buf.readString());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(perspective);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
