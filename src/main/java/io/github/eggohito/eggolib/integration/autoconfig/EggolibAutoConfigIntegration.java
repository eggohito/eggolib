package io.github.eggohito.eggolib.integration.autoconfig;

import io.github.eggohito.eggolib.integration.autoconfig.provider.EggolibGuiProviders;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigManager;
import me.shedaniel.autoconfig.gui.ConfigScreenProvider;
import me.shedaniel.autoconfig.gui.DefaultGuiProviders;
import me.shedaniel.autoconfig.gui.DefaultGuiTransformers;
import me.shedaniel.autoconfig.gui.registry.ComposedGuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.DefaultGuiRegistryAccess;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.gui.registry.api.GuiRegistryAccess;
import net.minecraft.client.gui.screen.Screen;

import java.util.function.Supplier;

public class EggolibAutoConfigIntegration {

	private static final GuiRegistry GUI_REGISTRY = DefaultGuiTransformers.apply(DefaultGuiProviders.apply(EggolibGuiProviders.apply(new GuiRegistry())));

	@SuppressWarnings("UnstableApiUsage")
	public static <T extends ConfigData>Supplier<Screen> getConfigScreen(Class<T> configClass, Screen parent) {
		GuiRegistryAccess guiRegistryAccess = new ComposedGuiRegistryAccess(AutoConfig.getGuiRegistry(configClass), GUI_REGISTRY, new DefaultGuiRegistryAccess());
		return new ConfigScreenProvider<>((ConfigManager<?>) AutoConfig.getConfigHolder(configClass), guiRegistryAccess, parent);
	}

}
