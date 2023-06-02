package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

public record CloseScreenPacket() implements FabricPacket {

	public static final PacketType<CloseScreenPacket> TYPE = PacketType.create(
		Eggolib.identifier("close_screen"), CloseScreenPacket::create
	);

	private static CloseScreenPacket create(PacketByteBuf buf) {
		return new CloseScreenPacket();
	}

	@Override
	public void write(PacketByteBuf buf) {

	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		((ClientPlayerEntityAccessor) player).getClient().setScreen(null);
	}

}
