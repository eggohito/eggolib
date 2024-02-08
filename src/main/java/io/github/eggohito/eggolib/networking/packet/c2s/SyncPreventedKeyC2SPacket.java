package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public record SyncPreventedKeyC2SPacket(String key, List<Identifier> powerIds) implements FabricPacket {

	public static final PacketType<SyncPreventedKeyC2SPacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_prevented_key"), SyncPreventedKeyC2SPacket::create
	);

	private static SyncPreventedKeyC2SPacket create(PacketByteBuf buf) {

		String key = buf.readString();
		List<Identifier> powerIds = new LinkedList<>();

		int powerIdsSize = buf.readInt();
		for (int i = 0; i < powerIdsSize; i++) {
			powerIds.add(buf.readIdentifier());
		}

		return new SyncPreventedKeyC2SPacket(key, powerIds);

	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(key);
		buf.writeInt(powerIds.size());
		powerIds.forEach(buf::writeIdentifier);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
