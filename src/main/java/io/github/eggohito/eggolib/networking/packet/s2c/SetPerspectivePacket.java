package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;

public record SetPerspectivePacket(EggolibPerspective perspective) implements FabricPacket {

	public static final PacketType<SetPerspectivePacket> TYPE = PacketType.create(
		Eggolib.identifier("set_perspective"), SetPerspectivePacket::create
	);

	private static SetPerspectivePacket create(PacketByteBuf buf) {
		return new SetPerspectivePacket(Enum.valueOf(EggolibPerspective.class, buf.readString()));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(perspective.toString());
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity player, PacketSender responseSender) {
		MiscUtilClient.setPerspective(((ClientPlayerEntityAccessor) player).getClient(), perspective);
	}

}
