package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.ScreenState;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public record SyncScreenStatePacket(ScreenState screenState) implements FabricPacket {

	public static final PacketType<SyncScreenStatePacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_screen_state"), SyncScreenStatePacket::create
	);

	private static SyncScreenStatePacket create(PacketByteBuf buf) {

		boolean inScreen = buf.readBoolean();
		boolean unknownScreen = buf.readBoolean();

		ScreenState screenState = new ScreenState(inScreen, !unknownScreen ? buf.readString() : null);
		return new SyncScreenStatePacket(screenState);

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

	public void handle(ServerPlayerEntity player, PacketSender responseSender) {
		Eggolib.PLAYERS_SCREEN.put(player, screenState);
	}

}
