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
import io.github.eggohito.eggolib.networking.packet.c2s.ModifyMessageC2SPacket;
import io.github.eggohito.eggolib.util.chat.MessageReplacer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModifySentMessagePower extends Power implements Prioritized<ModifySentMessagePower> {

	private final List<MessageReplacer> messageReplacers;
	private final int priority;

	public ModifySentMessagePower(PowerType<?> type, LivingEntity entity, MessageReplacer messageReplacer, List<MessageReplacer> messageReplacers, int priority) {
		super(type, entity);

		this.messageReplacers = new LinkedList<>();
		this.priority = priority;

		if (messageReplacer != null) {
			this.messageReplacers.add(messageReplacer);
		}

		if (messageReplacers != null) {
			this.messageReplacers.addAll(messageReplacers);
		}

	}

	@Override
	public int getPriority() {
		return priority;
	}

	public boolean doesApply(String message) {
		return messageReplacers
			.stream()
			.anyMatch(filter -> filter.matches(message));
	}

	public String replaceMessage(String message) {

		for (MessageReplacer messageFilter : this.messageReplacers) {
			message = messageFilter.replace(message, entity);
		}

		return message;

	}

	@Environment(EnvType.CLIENT)
	public static String onSendingClientMessage(String message) {

		PlayerEntity player = MinecraftClient.getInstance().player;

		Map<Identifier, String> powersToSync = new LinkedHashMap<>();
		List<ModifySentMessagePower> powers = PowerHolderComponent.getPowers(player, ModifySentMessagePower.class)
			.stream()
			.sorted(Comparator.comparing(ModifySentMessagePower::getPriority))
			.toList();

		for (ModifySentMessagePower power : powers) {

			if (!power.doesApply(message)) {
				continue;
			}

			powersToSync.put(power.getType().getIdentifier(), message);
			message = power.replaceMessage(message);

		}

		if (!powersToSync.isEmpty()) {
			ClientPlayNetworking.send(new ModifyMessageC2SPacket(powersToSync));
		}

		return message;

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("modify_sent_message"),
			new SerializableData()
				.add("filter", EggolibDataTypes.MESSAGE_REPLACER, null)
				.add("filters", EggolibDataTypes.MESSAGE_REPLACERS, null)
				.add("priority", SerializableDataTypes.INT, 0),
			data -> (powerType, livingEntity) -> new ModifySentMessagePower(
				powerType,
				livingEntity,
				data.get("filter"),
				data.get("filters"),
				data.get("priority")
			)
		).allowCondition();
	}

}
