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

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncPreventedKeyPacket;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.util.Key;
import io.github.eggohito.eggolib.util.ScreenState;
import io.github.eggohito.eggolib.networking.packet.c2s.EndKeySequencePacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncKeyPressPacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncPerspectivePacket;
import io.github.eggohito.eggolib.networking.packet.c2s.SyncScreenStatePacket;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.text.Text;

public class EggolibPacketsC2S {

	public static void register() {
		if (Eggolib.config.server.performVersionCheck) {
			ServerLoginConnectionEvents.QUERY_START.register(EggolibPacketsC2S::handshake);
			ServerLoginNetworking.registerGlobalReceiver(EggolibPackets.HANDSHAKE, EggolibPacketsC2S::handleHandshakeReply);
		}
		ServerPlayNetworking.registerGlobalReceiver(SyncScreenStatePacket.TYPE, SyncScreenStatePacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SyncPerspectivePacket.TYPE, SyncPerspectivePacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SyncKeyPressPacket.TYPE, SyncKeyPressPacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(EndKeySequencePacket.TYPE, EndKeySequencePacket::handle);
		ServerPlayNetworking.registerGlobalReceiver(SyncPreventedKeyPacket.TYPE, SyncPreventedKeyPacket::handle);
	}

	private static void handshake(ServerLoginNetworkHandler serverLoginNetworkHandler, MinecraftServer minecraftServer, PacketSender packetSender, ServerLoginNetworking.LoginSynchronizer loginSynchronizer) {
		packetSender.sendPacket(EggolibPackets.HANDSHAKE, PacketByteBufs.empty());
	}

	private static void handleHandshakeReply(MinecraftServer minecraftServer, ServerLoginNetworkHandler serverLoginNetworkHandler, boolean understood, PacketByteBuf packetByteBuf, ServerLoginNetworking.LoginSynchronizer loginSynchronizer, PacketSender packetSender) {

		if (understood) {

			int clientSemVerLength = packetByteBuf.readInt();
			int[] clientSemVer = new int[clientSemVerLength];

			boolean mismatch = clientSemVerLength != Eggolib.semanticVersion.length;

			for (int i = 0; i < clientSemVerLength; i++) {
				clientSemVer[i] = packetByteBuf.readInt();
				if (i < clientSemVerLength - 1 && clientSemVer[i] != Eggolib.semanticVersion[i]) {
					mismatch = true;
				}
			}

			if (mismatch) {

				StringBuilder clientVersionString = new StringBuilder();

				for (int i = 0; i < clientSemVerLength; i++) {
					clientVersionString.append(clientSemVer[i]);
					if (i < clientSemVerLength - 1) {
						clientVersionString.append(".");
					}
				}

				serverLoginNetworkHandler.disconnect(Text.translatable("disconnect.eggolib.version_mismatch", Eggolib.version, clientVersionString));

			}

		} else {
			serverLoginNetworkHandler.disconnect(Text.translatable("This server requires you to install Eggolib (v%s) to join.", Eggolib.version));
		}

	}

}
