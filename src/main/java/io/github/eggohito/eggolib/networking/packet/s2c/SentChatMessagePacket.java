package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ActionOnSendingMessagePower;
import io.github.eggohito.eggolib.util.chat.MessagePhase;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SentChatMessagePacket(Map<Identifier, MessagePhase> powersToInvoke) implements FabricPacket {

	public static final PacketType<SentChatMessagePacket> TYPE = PacketType.create(
		Eggolib.identifier("s2c/send_chat_message"), SentChatMessagePacket::read
	);

	private static SentChatMessagePacket read(PacketByteBuf buf) {
		return new SentChatMessagePacket(buf.readMap(PacketByteBuf::readIdentifier, valueBuf -> valueBuf.readEnumConstant(MessagePhase.class)));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powersToInvoke, PacketByteBuf::writeIdentifier, PacketByteBuf::writeEnumConstant);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity clientPlayerEntity, PacketSender packetSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(clientPlayerEntity);
		for (Map.Entry<Identifier, MessagePhase> powerToInvoke : powersToInvoke.entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerToInvoke.getKey());

			if (component.getPower(powerType) instanceof ActionOnSendingMessagePower aocmp) {
				aocmp.executeActions(powerToInvoke.getValue());
			}

		}

	}

}
