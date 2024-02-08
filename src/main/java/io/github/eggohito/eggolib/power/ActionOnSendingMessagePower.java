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
import io.github.eggohito.eggolib.networking.packet.s2c.SendMessageS2CPacket;
import io.github.eggohito.eggolib.util.chat.MessageConsumer;
import io.github.eggohito.eggolib.util.chat.MessagePhase;
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

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
public class ActionOnSendingMessagePower extends Power implements Prioritized<ActionOnSendingMessagePower> {

	private final RegistryKey<MessageType> messageTypeKey;
	private final List<MessageConsumer> messageFilters;

	private final int priority;

	public ActionOnSendingMessagePower(PowerType<?> type, LivingEntity entity, RegistryKey<MessageType> messageTypeKey, MessageConsumer messageFilter, List<MessageConsumer> messageFilters, int priority) {
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

	public void executeActions(MessagePhase phase) {
		this.messageFilters.forEach(filter -> filter.execute(phase, entity));
	}

	public static boolean beforeSendingServerMessage(SignedMessage message, @Nullable ServerPlayerEntity sender, MessageType.Parameters params) {

		if (sender == null) {
			return true;
		}

		DynamicRegistryManager registryManager = sender.getWorld().getRegistryManager();
		Registry<MessageType> messageTypeRegistry = registryManager.get(RegistryKeys.MESSAGE_TYPE);

		Map<Identifier, MessagePhase> powersToSync = PowerHolderComponent.getPowers(sender, ActionOnSendingMessagePower.class)
			.stream()
			.filter(p -> p.doesApply(message.getContent().getString(), params.type(), messageTypeRegistry))
			.sorted(Comparator.comparing(ActionOnSendingMessagePower::getPriority))
			.peek(p -> p.executeActions(MessagePhase.BEFORE))
			.collect(Collectors.toMap(p -> p.getType().getIdentifier(), p -> MessagePhase.BEFORE, (o, o2) -> o2, LinkedHashMap::new));

		if (!powersToSync.isEmpty()) {
			ServerPlayNetworking.send(sender, new SendMessageS2CPacket(powersToSync));
		}

		return true;

	}

	public static void afterSendingServerMessage(SignedMessage message, @Nullable ServerPlayerEntity sender, MessageType.Parameters params) {

		if (sender == null) {
			return;
		}

		DynamicRegistryManager registryManager = sender.getWorld().getRegistryManager();
		Registry<MessageType> messageTypeRegistry = registryManager.get(RegistryKeys.MESSAGE_TYPE);

		Map<Identifier, MessagePhase> powersToSync = PowerHolderComponent.getPowers(sender, ActionOnSendingMessagePower.class)
			.stream()
			.filter(p -> p.doesApply(message.getContent().getString(), params.type(), messageTypeRegistry))
			.sorted(Comparator.comparing(ActionOnSendingMessagePower::getPriority))
			.peek(p -> p.executeActions(MessagePhase.AFTER))
			.collect(Collectors.toMap(p -> p.getType().getIdentifier(), p -> MessagePhase.AFTER, (o, o2) -> o2, LinkedHashMap::new));

		if (!powersToSync.isEmpty()) {
			ServerPlayNetworking.send(sender, new SendMessageS2CPacket(powersToSync));
		}

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("action_on_sending_message"),
			new SerializableData()
				.add("message_type", EggolibDataTypes.MESSAGE_TYPE, MessageType.CHAT)
				.add("filter", EggolibDataTypes.BACKWARDS_COMPATIBLE_FUNCTIONAL_MESSAGE_FILTER, null)
				.add("filters", EggolibDataTypes.BACKWARDS_COMPATIBLE_FUNCTIONAL_MESSAGE_FILTERS, null)
				.add("priority", SerializableDataTypes.INT, 0),
			data -> (powerType, livingEntity) -> new ActionOnSendingMessagePower(
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
