package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SyncKeyPressC2SPacket(Map<Identifier, String> powerAndKeyMap) implements FabricPacket {

	public static final PacketType<SyncKeyPressC2SPacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_key_press"), SyncKeyPressC2SPacket::create
	);

	private static SyncKeyPressC2SPacket create(PacketByteBuf buf) {
		return new SyncKeyPressC2SPacket(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readString));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powerAndKeyMap, PacketByteBuf::writeIdentifier, PacketByteBuf::writeString);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
