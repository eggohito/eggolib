package io.github.eggohito.eggolib.util.key;

import io.github.eggohito.eggolib.util.Key;
import net.minecraft.entity.Entity;

import java.util.function.Consumer;

public class FunctionalKey extends Key {

	public Consumer<Entity> action = null;

	public FunctionalKey(String key) {
		super(key);
	}

	public FunctionalKey(String key, boolean continuous, Consumer<Entity> action) {
		super(key, continuous);
		this.action = action;
	}

	public void function(Entity entity) {
		if (action != null) {
			action.accept(entity);
		}
	}

}
