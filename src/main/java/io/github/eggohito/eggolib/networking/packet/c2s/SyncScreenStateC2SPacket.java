package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.ScreenState;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;

public record SyncScreenStateC2SPacket(ScreenState screenState) implements FabricPacket {

	public static final PacketType<SyncScreenStateC2SPacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_screen_state"), SyncScreenStateC2SPacket::create
	);

	private static SyncScreenStateC2SPacket create(PacketByteBuf buf) {

		boolean inScreen = buf.readBoolean();
		boolean unknownScreen = buf.readBoolean();

		ScreenState screenState = new ScreenState(inScreen, !unknownScreen ? buf.readString() : null);
		return new SyncScreenStateC2SPacket(screenState);

	}

	@Override
	public void write(PacketByteBuf buf) {

		boolean inScreen = screenState.isInScreen();
		boolean unknownScreen = screenState.getScreenClassName() == null;

		buf.writeBoolean(inScreen);
		buf.writeBoolean(unknownScreen);

		if (!unknownScreen) {
			buf.writeString(screenState.getScreenClassName());
		}

	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

}
