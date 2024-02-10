package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.Prioritized;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.networking.packet.s2c.PreventSendMessageS2CPacket;
import io.github.eggohito.eggolib.util.chat.MessageFilter;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PreventSendingMessagePower extends Power implements Prioritized<PreventSendingMessagePower> {

	private final RegistryKey<MessageType> messageTypeKey;
	private final List<MessageFilter> messageFilters;

	private final int priority;

	public PreventSendingMessagePower(PowerType<?> type, LivingEntity entity, RegistryKey<MessageType> messageTypeKey, MessageFilter messageFilter, List<MessageFilter> messageFilters, int priority) {
		super(type, entity);

		this.messageTypeKey = messageTypeKey;
		this.messageFilters = new LinkedList<>();
		this.priority = priority;

		if (messageFilter != null) {
			this.messageFilters.add(messageFilter);
		}

		if (messageFilters != null) {
			this.messageFilters.addAll(messageFilters);
		}

	}

	@Override
	public int getPriority() {
		return priority;
	}

	public boolean doesApply(String message) {
		return messageFilters.isEmpty() || messageFilters
			.stream()
			.anyMatch(filter -> filter.matches(message));
	}

	public boolean doesApply(String message, MessageType messageType, Registry<MessageType> messageTypeRegistry) {
		MessageType specifiedMessageType = messageTypeRegistry.get(messageTypeKey);
		return Objects.equals(specifiedMessageType, messageType)
			&& this.doesApply(message);
	}

	public void executeActions(String message) {
		messageFilters
			.stream()
			.filter(mf -> mf.matches(message))
			.forEach(mf -> mf.execute(entity));
	}

	public static boolean onSendServerMessage(SignedMessage message, @Nullable ServerPlayerEntity sender, MessageType.Parameters params) {

		if (sender == null) {
			return true;
		}

		DynamicRegistryManager registryManager = sender.getWorld().getRegistryManager();
		Registry<MessageType> messageTypeRegistry = registryManager.get(RegistryKeys.MESSAGE_TYPE);

		String messageString = message.getContent().getString();
		List<Identifier> powers = PowerHolderComponent.getPowers(sender, PreventSendingMessagePower.class)
			.stream()
			.filter(p -> p.doesApply(messageString, params.type(), messageTypeRegistry))
			.sorted(Comparator.comparing(PreventSendingMessagePower::getPriority).reversed())
			.peek(p -> p.executeActions(messageString))
			.map(p -> p.getType().getIdentifier())
			.toList();

		if (!powers.isEmpty()) {
			ServerPlayNetworking.send(sender, new PreventSendMessageS2CPacket(powers, messageString));
			return false;
		}

		return true;

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("prevent_sending_message"),
			new SerializableData()
				.add("message_type", EggolibDataTypes.MESSAGE_TYPE, MessageType.CHAT)
				.add("filter", EggolibDataTypes.BACKWARDS_COMPATIBLE_MESSAGE_FILTER, null)
				.add("filters", EggolibDataTypes.BACKWARDS_COMPATIBLE_MESSAGE_FILTERS, null)
				.add("priority", SerializableDataTypes.INT, 0),
			data -> (powerType, livingEntity) -> new PreventSendingMessagePower(
				powerType,
				livingEntity,
				data.get("message_type"),
				data.get("filter"),
				data.get("filters"),
				data.get("priority")
			)
		).allowCondition();
	}

}
