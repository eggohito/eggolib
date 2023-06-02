package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public record SyncPerspectivePacket(String perspective) implements FabricPacket {

	public static final PacketType<SyncPerspectivePacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_perspective"), SyncPerspectivePacket::create
	);

	private static SyncPerspectivePacket create(PacketByteBuf buf) {
		return new SyncPerspectivePacket(buf.readString());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(perspective);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ServerPlayerEntity player, PacketSender responseSender) {
		Eggolib.PLAYERS_PERSPECTIVE.put(player, perspective);
	}

}
