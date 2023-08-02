package io.github.eggohito.eggolib.util.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "client")
public class EggolibConfigClient implements ConfigData {

	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.BoundedDiscrete(min = 5, max = Integer.MAX_VALUE)
	public int syncTickRate = 10;

	@Override
	public void validatePostLoad() throws ValidationException {
		if (syncTickRate < 5) {
			syncTickRate = 5;
		}
	}

}
