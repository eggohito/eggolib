package io.github.eggohito.eggolib.util.chat;

import net.minecraft.entity.Entity;

import java.util.function.Consumer;
import java.util.regex.Pattern;

public class MessageFilter {

	private final Pattern filter;
	private final Consumer<Entity> entityAction;

	public MessageFilter(Pattern filter, Consumer<Entity> entityAction) {
		this.filter = filter;
		this.entityAction = entityAction;
	}

	public Pattern getFilter() {
		return filter;
	}

	public Consumer<Entity> getAction() {
		return entityAction;
	}

	public boolean matches(String message) {
		return filter.pattern().equals(message) || filter.matcher(message).find();
	}

	public void execute(Entity entity) {
		if (entityAction != null) {
			entityAction.accept(entity);
		}
	}

}
