package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.PreventSendingMessagePower;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public record PreventedChatMessagePacket(List<Identifier> powersToInvoke) implements FabricPacket {

	public static final PacketType<PreventedChatMessagePacket> TYPE = PacketType.create(
		Eggolib.identifier("s2c/prevent_chat_message"), PreventedChatMessagePacket::read
	);

	private static PreventedChatMessagePacket read(PacketByteBuf buf) {
		return new PreventedChatMessagePacket(buf.readCollection(ArrayList::new, PacketByteBuf::readIdentifier));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeCollection(powersToInvoke, PacketByteBuf::writeIdentifier);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity clientPlayerEntity, PacketSender packetSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(clientPlayerEntity);
		for (Identifier powerToInvoke : powersToInvoke) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerToInvoke);

			if (component.getPower(powerType) instanceof PreventSendingMessagePower pcmp) {
				pcmp.executeActions();
			}

		}

	}

}
