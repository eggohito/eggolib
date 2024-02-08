package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EggolibPerspective;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record SetPerspectiveS2CPacket(EggolibPerspective perspective) implements FabricPacket {

	public static final PacketType<SetPerspectiveS2CPacket> TYPE = PacketType.create(
		Eggolib.identifier("set_perspective"), SetPerspectiveS2CPacket::create
	);

	private static SetPerspectiveS2CPacket create(PacketByteBuf buf) {
		return new SetPerspectiveS2CPacket(Enum.valueOf(EggolibPerspective.class, buf.readString()));
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(perspective.toString());
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
