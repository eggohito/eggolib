package io.github.eggohito.eggolib.util.key;

import io.github.eggohito.eggolib.util.Key;
import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public class FunctionalKey extends Key {

	public boolean continuous = false;
	public Consumer<Entity> action = null;

	public FunctionalKey() {
	}

	public FunctionalKey(String key, boolean continuous) {
		this.key = key;
		this.continuous = continuous;
	}

	public FunctionalKey(String key, boolean continuous, Consumer<Entity> action) {
		this.key = key;
		this.continuous = continuous;
		this.action = action;
	}

	public void function(Entity entity) {
		if (action != null) {
			action.accept(entity);
		}
	}

}
