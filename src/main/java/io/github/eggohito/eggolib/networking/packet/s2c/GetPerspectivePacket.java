package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

public record GetPerspectivePacket() implements FabricPacket {

	public static final PacketType<GetPerspectivePacket> TYPE = PacketType.create(
		Eggolib.identifier("get_perspective"), GetPerspectivePacket::create
	);

	private static GetPerspectivePacket create(PacketByteBuf buf) {
		return new GetPerspectivePacket();
	}

	@Override
	public void write(PacketByteBuf buf) {

	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		MiscUtilClient.getPerspective(((ClientPlayerEntityAccessor) player).getClient());
	}

}
