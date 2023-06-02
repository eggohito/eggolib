package io.github.eggohito.eggolib.mixin;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessor {

	@Accessor("KEYS_BY_ID")
	static Map<String, KeyBinding> getIdAndKeyMap() {
		throw new AssertionError("This is not supposed to be thrown! :(");
	}

	@Accessor("KEY_TO_BINDINGS")
	static Map<InputUtil.Key, KeyBinding> getKeyAndBindingMap() {
		throw new AssertionError("This is not supposed to be thrown! :(");
	}

}
