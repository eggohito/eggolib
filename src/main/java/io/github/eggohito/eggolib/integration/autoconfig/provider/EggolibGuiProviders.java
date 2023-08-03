package io.github.eggohito.eggolib.integration.autoconfig.provider;

import io.github.eggohito.eggolib.integration.autoconfig.annotation.NonSliderBoundedDiscrete;
import me.shedaniel.autoconfig.gui.registry.GuiRegistry;
import me.shedaniel.autoconfig.util.Utils;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.LongListEntry;
import net.minecraft.text.Text;

import java.util.Collections;

public class EggolibGuiProviders {

	private static final ConfigEntryBuilder ENTRY_BUILDER = ConfigEntryBuilder.create();

	public static GuiRegistry apply(GuiRegistry registry) {

		registry.registerAnnotationProvider(
			(i18n, field, config, defaults, guiRegistryAccess) -> {

				NonSliderBoundedDiscrete.Integer bounds = field.getAnnotation(NonSliderBoundedDiscrete.Integer.class);
				IntegerListEntry entry = ENTRY_BUILDER
					.startIntField(Text.translatable(i18n), Utils.getUnsafely(field, config, 0))
					.setDefaultValue(() -> Utils.getUnsafely(field, defaults))
					.setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
					.setMin(bounds.min())
					.setMax(bounds.max())
					.build();

				return Collections.singletonList(entry);

			},
			field -> field.getType() == Integer.TYPE || field.getType() == Integer.class,
			NonSliderBoundedDiscrete.Integer.class
		);

		registry.registerAnnotationProvider(
			(i18n, field, config, defaults, guiRegistryAccess) -> {

				NonSliderBoundedDiscrete.Long bounds = field.getAnnotation(NonSliderBoundedDiscrete.Long.class);
				LongListEntry entry = ENTRY_BUILDER
					.startLongField(Text.translatable(i18n), Utils.getUnsafely(field, config, 0L))
					.setDefaultValue(() -> Utils.getUnsafely(field, defaults))
					.setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
					.setMin(bounds.min())
					.setMax(bounds.max())
					.build();

				return Collections.singletonList(entry);

			},
			field -> field.getType() == Long.TYPE || field.getType() == Long.class,
			NonSliderBoundedDiscrete.Long.class
		);

		registry.registerAnnotationProvider(
			(i18n, field, config, defaults, guiRegistryAccess) -> {

				NonSliderBoundedDiscrete.Float bounds = field.getAnnotation(NonSliderBoundedDiscrete.Float.class);
				FloatListEntry entry = ENTRY_BUILDER
					.startFloatField(Text.translatable(i18n), Utils.getUnsafely(field, config, 0f))
					.setDefaultValue(() -> Utils.getUnsafely(field, defaults))
					.setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
					.setMin(bounds.min())
					.setMax(bounds.max())
					.build();

				return Collections.singletonList(entry);

			},
			field -> field.getType() == Float.TYPE || field.getType() == Float.class,
			NonSliderBoundedDiscrete.Float.class
		);

		registry.registerAnnotationProvider(
			(i18n, field, config, defaults, guiRegistryAccess) -> {

				NonSliderBoundedDiscrete.Double bounds = field.getAnnotation(NonSliderBoundedDiscrete.Double.class);
				DoubleListEntry entry = ENTRY_BUILDER
					.startDoubleField(Text.translatable(i18n), Utils.getUnsafely(field, config, 0d))
					.setDefaultValue(() -> Utils.getUnsafely(field, defaults))
					.setSaveConsumer(newValue -> Utils.setUnsafely(field, config, newValue))
					.setMin(bounds.min())
					.setMax(bounds.max())
					.build();

				return Collections.singletonList(entry);

			},
			field -> field.getType() == Double.TYPE || field.getType() == Double.class,
			NonSliderBoundedDiscrete.Double.class
		);

		return registry;

	}

}
