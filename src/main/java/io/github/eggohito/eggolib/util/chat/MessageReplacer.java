package io.github.eggohito.eggolib.util.chat;

import net.minecraft.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class MessageReplacer extends MessageConsumer {

	@Nullable
	private final String replacement;

	public MessageReplacer(Pattern filter, @Nullable String replacement, Consumer<Entity> beforeAction, Consumer<Entity> afterAction) {
		super(filter, beforeAction, afterAction);
		this.replacement = replacement;
	}

	@Nullable
	public String getReplacement() {
		return replacement;
	}

	public String replace(String message, Entity entity) {

		String newMessage = message;
		if (!this.matches(message)) {
			return newMessage;
		}

		String interMessage = newMessage;
		this.execute(MessagePhase.BEFORE, entity);

		if (replacement != null) {
			newMessage = this.getFilter().matcher(message).replaceAll(replacement);
		}

		if (!interMessage.equals(newMessage)) {
			this.execute(MessagePhase.AFTER, entity);
		}

		return newMessage;

	}

}
