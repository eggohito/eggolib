package io.github.eggohito.eggolib.compat;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public abstract class EggolibModCompatClient extends EggolibModCompat {

	private final Map<String, Class<? extends Screen>> REGISTERED_SCREEN_MAPPINGS = new HashMap<>();

	public final void addScreenMapping(String name, Class<? extends Screen> clazz) {
		ClassDataRegistry
			.getOrCreate(ClassUtil.castClass(Screen.class), "Screen")
			.addMapping(name, clazz);
		REGISTERED_SCREEN_MAPPINGS.put(name, clazz);
	}

	public final String formatScreenMappings() {

		StringBuilder builder = new StringBuilder();
		REGISTERED_SCREEN_MAPPINGS.forEach(
			(s, aClass) -> builder.append("\t- ").append(aClass.getName()).append("\t(mapped as \"").append(s).append("\")\n")
		);

		return builder.toString();

	}

}
