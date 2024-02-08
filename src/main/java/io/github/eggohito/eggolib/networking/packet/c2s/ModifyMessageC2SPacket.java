package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public record ModifyMessageC2SPacket(Map<Identifier, String> powersToInvoke) implements FabricPacket {

	public static final PacketType<ModifyMessageC2SPacket> TYPE = PacketType.create(
		Eggolib.identifier("c2s/modified_chat_message"), ModifyMessageC2SPacket::read
	);

	private static ModifyMessageC2SPacket read(PacketByteBuf buf) {
		return new ModifyMessageC2SPacket(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readString));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powersToInvoke, PacketByteBuf::writeIdentifier, PacketByteBuf::writeString);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
