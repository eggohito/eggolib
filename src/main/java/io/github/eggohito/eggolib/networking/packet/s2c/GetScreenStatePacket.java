package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

public record GetScreenStatePacket() implements FabricPacket {

	public static final PacketType<GetScreenStatePacket> TYPE = PacketType.create(
		Eggolib.identifier("get_screen_state"), GetScreenStatePacket::create
	);

	private static GetScreenStatePacket create(PacketByteBuf buf) {
		return new GetScreenStatePacket();
	}

	@Override
	public void write(PacketByteBuf buf) {

	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		MiscUtilClient.getScreenState(((ClientPlayerEntityAccessor) player).getClient());
	}

}
