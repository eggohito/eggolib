/*
    MIT License

    Copyright (c) 2021 apace100

    Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
    to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
    and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY
    , FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR
    OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
    DEALINGS IN THE SOFTWARE.
 */

package io.github.eggohito.eggolib.networking;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.networking.packet.VersionHandshakePacket;
import io.github.eggohito.eggolib.networking.packet.c2s.*;
import io.github.eggohito.eggolib.networking.packet.task.VersionHandshakeTask;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.power.ModifySentMessagePower;
import io.github.eggohito.eggolib.power.PreventKeyUsePower;
import io.github.eggohito.eggolib.util.Key;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerConfigurationNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;

public class EggolibPacketsC2S {

	public static void register() {

		if (Eggolib.config.server.performVersionCheck) {
			ServerConfigurationConnectionEvents.CONFIGURE.register(EggolibPacketsC2S::configureVersionHandshake);
			ServerConfigurationNetworking.registerGlobalReceiver(VersionHandshakePacket.TYPE, EggolibPacketsC2S::onVersionHandshake);
		}

		ServerPlayConnectionEvents.INIT.register((handler, server) -> {
			ServerPlayNetworking.registerReceiver(handler, SyncScreenStateC2SPacket.TYPE, EggolibPacketsC2S::onScreenStateSync);
			ServerPlayNetworking.registerReceiver(handler, SyncPerspectiveC2SPacket.TYPE, EggolibPacketsC2S::onPerspectiveSync);
			ServerPlayNetworking.registerReceiver(handler, SyncKeyPressC2SPacket.TYPE, EggolibPacketsC2S::onKeyPressSync);
			ServerPlayNetworking.registerReceiver(handler, EndKeySequenceC2SPacket.TYPE, EggolibPacketsC2S::onKeySequenceEnd);
			ServerPlayNetworking.registerReceiver(handler, SyncPreventedKeyC2SPacket.TYPE, EggolibPacketsC2S::onPreventedKeySync);
			ServerPlayNetworking.registerReceiver(handler, ModifyMessageC2SPacket.TYPE, EggolibPacketsC2S::onMessageModify);
		});

	}

	private static void onMessageModify(ModifyMessageC2SPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Map.Entry<Identifier, String> powerToInvoke : packet.powersToInvoke().entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerToInvoke.getKey());

			if (component.getPower(powerType) instanceof ModifySentMessagePower msmp) {
				msmp.replaceMessage(powerToInvoke.getValue());
			}

		}

	}

	private static void onPreventedKeySync(SyncPreventedKeyC2SPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Identifier powerId : packet.powerIds()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerId);

			if (component.getPower(powerType) instanceof PreventKeyUsePower pkup) {
				pkup.executeActions(packet.key());
			}

		}

	}

	private static void onKeySequenceEnd(EndKeySequenceC2SPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Map.Entry<Identifier, Boolean> powerAndResult : packet.powerAndResultMap().entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerAndResult.getKey());

			if (!(component.getPower(powerType) instanceof ActionOnKeySequencePower aoksp)) {
				continue;
			}

			if (powerAndResult.getValue()) {
				aoksp.onSuccess();
			}

			else {
				aoksp.onFail();
			}

		}

	}

	private static void onKeyPressSync(SyncKeyPressC2SPacket packet, ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Map.Entry<Identifier, String> powerAndKey : packet.powerAndKeyMap().entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerAndKey.getKey());

			if (component.getPower(powerType) instanceof ActionOnKeySequencePower aoksp) {
				aoksp.addKeyToSequence(new Key(powerAndKey.getValue()));
			}

		}

	}

	private static void onPerspectiveSync(SyncPerspectiveC2SPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
		Eggolib.PLAYERS_PERSPECTIVE.put(player, packet.perspective());
	}

	private static void onScreenStateSync(SyncScreenStateC2SPacket packet, ServerPlayerEntity player, PacketSender responseSender) {
		Eggolib.PLAYERS_SCREEN.put(player, packet.screenState());
	}

	private static void configureVersionHandshake(ServerConfigurationNetworkHandler handler, MinecraftServer server) {

		if (ServerConfigurationNetworking.canSend(handler, VersionHandshakePacket.TYPE)) {
			handler.addTask(new VersionHandshakeTask(Eggolib.semanticVersion));
		}

		else {
			handler.disconnect(Text.of("This server requires you to install eggolib (v" + Eggolib.version + ") to play."));
		}

	}

	private static void onVersionHandshake(VersionHandshakePacket packet, ServerConfigurationNetworkHandler networkHandler, PacketSender responseSender) {

		boolean mismatch = packet.semver().length != Eggolib.semanticVersion.length;
		for (int i = 0; !mismatch && i < packet.semver().length - 1; i++) {

			if (packet.semver()[i] != Eggolib.semanticVersion[i]) {
				mismatch = true;
				break;
			}

		}

		if (!mismatch) {
			networkHandler.completeTask(VersionHandshakeTask.KEY);
			return;
		}

		StringBuilder semverString = new StringBuilder();
		String separator = "";

		for (int i : packet.semver()) {
			semverString.append(separator).append(i);
			separator = ".";
		}

		networkHandler.disconnect(Text.translatable("disconnect.eggolib.version_mismatch", Eggolib.version, semverString.toString()));

	}

}
