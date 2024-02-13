package io.github.eggohito.eggolib.util.chat;

import net.minecraft.entity.Entity;

import java.util.EnumMap;
import java.util.function.Consumer;

public class MessageConsumer extends MessageFilter {

	private final EnumMap<MessagePhase, Consumer<Entity>> actions;

	public MessageConsumer(String filter, Consumer<Entity> beforeAction, Consumer<Entity> afterAction) {
		super(filter);
		this.actions = new EnumMap<>(MessagePhase.class);

		if (beforeAction != null) {
			this.actions.put(MessagePhase.BEFORE, beforeAction);
		}

		if (afterAction != null) {
			this.actions.put(MessagePhase.AFTER, afterAction);
		}

	}

	public Consumer<Entity> getAction(MessagePhase messagePhase) {
		return actions.get(messagePhase);
	}

	public void execute(MessagePhase messagePhase, Entity entity) {

		if (actions.containsKey(messagePhase)) {
			this.getAction(messagePhase).accept(entity);
		}

	}

}
