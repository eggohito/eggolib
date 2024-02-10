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
import io.github.eggohito.eggolib.access.InventoryHolder;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.networking.packet.VersionHandshakePacket;
import io.github.eggohito.eggolib.networking.packet.s2c.*;
import io.github.eggohito.eggolib.power.ActionOnSendingMessagePower;
import io.github.eggohito.eggolib.power.PreventSendingMessagePower;
import io.github.eggohito.eggolib.util.MiscUtilClient;
import io.github.eggohito.eggolib.util.chat.MessagePhase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class EggolibPacketsS2C {

	public static void register() {

		ClientConfigurationNetworking.registerGlobalReceiver(VersionHandshakePacket.TYPE, EggolibPacketsS2C::initVersionHandshake);

		ClientPlayConnectionEvents.INIT.register((handler, client) -> {
			ClientPlayNetworking.registerReceiver(CloseScreenS2CPacket.TYPE, EggolibPacketsS2C::onScreenClose);
			ClientPlayNetworking.registerReceiver(GetScreenStateS2CPacket.TYPE, EggolibPacketsS2C::onScreenStateQuery);
			ClientPlayNetworking.registerReceiver(SetPerspectiveS2CPacket.TYPE, EggolibPacketsS2C::onPerspectiveSet);
			ClientPlayNetworking.registerReceiver(GetPerspectiveS2CPacket.TYPE, EggolibPacketsS2C::onPerspectiveQuery);
			ClientPlayNetworking.registerReceiver(OpenInventoryS2CPacket.TYPE, EggolibPacketsS2C::onInventoryOpen);
			ClientPlayNetworking.registerReceiver(PreventSendMessageS2CPacket.TYPE, EggolibPacketsS2C::onMessagePrevent);
			ClientPlayNetworking.registerReceiver(SendMessageS2CPacket.TYPE, EggolibPacketsS2C::onMessageSend);
		});

	}

	private static void onMessageSend(SendMessageS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Map.Entry<Identifier, MessagePhase> powerToInvoke : packet.powersToInvoke().entrySet()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerToInvoke.getKey());

			if (component.getPower(powerType) instanceof ActionOnSendingMessagePower aosmp) {
				aosmp.executeActions(powerToInvoke.getValue(), packet.message());
			}

		}

	}

	private static void onMessagePrevent(PreventSendMessageS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Identifier powerToInvoke : packet.powersToInvoke()) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerToInvoke);

			if (component.getPower(powerType) instanceof PreventSendingMessagePower psmp) {
				psmp.executeActions(packet.message());
			}

		}

	}

	private static void onInventoryOpen(OpenInventoryS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {

		Entity entity = player.getWorld().getEntityById(packet.entityId());
		if (entity instanceof InventoryHolder inventoryHolder) {
			inventoryHolder.eggolib$openInventory();
		}

		else {
			Eggolib.LOGGER.warn("Received packet for opening the inventory of {}!", entity == null ? "unknown entity" : "entity without an inventory");
		}

	}

	private static void onPerspectiveQuery(GetPerspectiveS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
		MiscUtilClient.getPerspective(((ClientPlayerEntityAccessor) player).getClient());
	}

	private static void onPerspectiveSet(SetPerspectiveS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
		MiscUtilClient.setPerspective(((ClientPlayerEntityAccessor) player).getClient(), packet.perspective());
	}

	private static void onScreenStateQuery(GetScreenStateS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
		MiscUtilClient.getScreenState(((ClientPlayerEntityAccessor) player).getClient());
	}

	private static void onScreenClose(CloseScreenS2CPacket packet, ClientPlayerEntity player, PacketSender responseSender) {
		((ClientPlayerEntityAccessor) player).getClient().setScreen(null);
	}

	private static void initVersionHandshake(VersionHandshakePacket packet, PacketSender responseSender) {
		responseSender.sendPacket(new VersionHandshakePacket(Eggolib.semanticVersion));
	}

}
