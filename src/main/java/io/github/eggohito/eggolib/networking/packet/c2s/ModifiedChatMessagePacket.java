package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ModifySentMessagePower;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Map;

public record ModifiedChatMessagePacket(Map<Identifier, String> powersToInvoke) implements FabricPacket {

	public static final PacketType<ModifiedChatMessagePacket> TYPE = PacketType.create(
		Eggolib.identifier("c2s/modified_chat_message"), ModifiedChatMessagePacket::read
	);

	private static ModifiedChatMessagePacket read(PacketByteBuf buf) {
		return new ModifiedChatMessagePacket(buf.readMap(PacketByteBuf::readIdentifier, PacketByteBuf::readString));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeMap(powersToInvoke, PacketByteBuf::writeIdentifier, PacketByteBuf::writeString);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ServerPlayerEntity serverPlayer, PacketSender packetSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(serverPlayer);
		for (Map.Entry<Identifier, String> powerToInvoke : powersToInvoke.entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerToInvoke.getKey());

			if (component.getPower(powerType) instanceof ModifySentMessagePower mcmp) {
				mcmp.replaceMessage(powerToInvoke.getValue());
			}

		}

	}

}
