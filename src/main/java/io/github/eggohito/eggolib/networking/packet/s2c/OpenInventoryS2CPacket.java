package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record OpenInventoryS2CPacket(int entityId) implements FabricPacket {

	public static final PacketType<OpenInventoryS2CPacket> TYPE = PacketType.create(
		Eggolib.identifier("s2c/open_inventory"), OpenInventoryS2CPacket::read
	);

	private static OpenInventoryS2CPacket read(PacketByteBuf buf) {
		return new OpenInventoryS2CPacket(buf.readVarInt());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeVarInt(entityId);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
