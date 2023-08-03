package io.github.eggohito.eggolib.util.config;

import io.github.eggohito.eggolib.integration.autoconfig.annotation.NonSliderBoundedDiscrete;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "client")
public class EggolibConfigClient implements ConfigData {

	@ConfigEntry.Gui.Tooltip
	@NonSliderBoundedDiscrete.Integer(min = 5)
	public int syncTickRate = 10;

}
