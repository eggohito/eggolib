package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.chat.MessagePhase;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SendMessageS2CPacket(Map<Identifier, MessagePhase> powersToInvoke, String message) implements FabricPacket {

	public static final PacketType<SendMessageS2CPacket> TYPE = PacketType.create(
		Eggolib.identifier("s2c/send_chat_message"), SendMessageS2CPacket::read
	);

	private static SendMessageS2CPacket read(PacketByteBuf buf) {
		return new SendMessageS2CPacket(
			buf.readMap(PacketByteBuf::readIdentifier, valueBuf -> valueBuf.readEnumConstant(MessagePhase.class)),
			buf.readString()
		);
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powersToInvoke, PacketByteBuf::writeIdentifier, PacketByteBuf::writeEnumConstant);
		buf.writeString(message);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
