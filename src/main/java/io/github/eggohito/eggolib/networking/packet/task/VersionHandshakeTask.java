package io.github.eggohito.eggolib.networking.packet.task;

import io.github.eggohito.eggolib.networking.packet.VersionHandshakePacket;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationNetworking;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayerConfigurationTask;

import java.util.function.Consumer;

public record VersionHandshakeTask(int[] semver) implements ServerPlayerConfigurationTask {

	public static final ServerPlayerConfigurationTask.Key KEY = new ServerPlayerConfigurationTask.Key("eggolib:handshake/version");

	@Override
	public void sendPacket(Consumer<Packet<?>> sender) {
		sender.accept(ServerConfigurationNetworking.createS2CPacket(new VersionHandshakePacket(semver)));
	}

	@Override
	public Key getKey() {
		return KEY;
	}

}
