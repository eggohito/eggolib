package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record PreventSendMessageS2CPacket(List<Identifier> powersToInvoke, String message) implements FabricPacket {

	public static final PacketType<PreventSendMessageS2CPacket> TYPE = PacketType.create(
		Eggolib.identifier("s2c/prevent_chat_message"), PreventSendMessageS2CPacket::read
	);

	private static PreventSendMessageS2CPacket read(PacketByteBuf buf) {
		return new PreventSendMessageS2CPacket(
			buf.readCollection(ArrayList::new, PacketByteBuf::readIdentifier),
			buf.readString()
		);
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeCollection(powersToInvoke, PacketByteBuf::writeIdentifier);
		buf.writeString(message);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
