package io.github.eggohito.eggolib.integration;

import dev.micalobia.breathinglib.event.BreathingCallback;
import io.github.eggohito.eggolib.power.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;

public class EggolibPowerIntegration {

	public static void register() {
		AttackBlockCallback.EVENT.register(ActionOnBlockHitPower::integrateCallback);
		BreathingCallback.EVENT.register(ModifyBreathingPower::integrateCallback);
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(ActionOnSendingMessagePower::beforeSendingServerMessage);
		ServerMessageEvents.ALLOW_CHAT_MESSAGE.register(PreventSendingMessagePower::onSendServerMessage);
		ServerMessageEvents.ALLOW_COMMAND_MESSAGE.register((message, source, params) -> ActionOnSendingMessagePower.beforeSendingServerMessage(message, source.getPlayer(), params));
		ServerMessageEvents.ALLOW_COMMAND_MESSAGE.register((message, source, params) -> PreventSendingMessagePower.onSendServerMessage(message, source.getPlayer(), params));
		ServerMessageEvents.CHAT_MESSAGE.register(ActionOnSendingMessagePower::afterSendingServerMessage);
		ServerMessageEvents.COMMAND_MESSAGE.register((message, source, params) -> ActionOnSendingMessagePower.afterSendingServerMessage(message, source.getPlayer(), params));
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		ClientTickEvents.START_CLIENT_TICK.register(ActionOnKeySequencePower::integrateCallback);
		ClientSendMessageEvents.MODIFY_CHAT.register(ModifySentMessagePower::onSendingClientMessage);
	}

}
