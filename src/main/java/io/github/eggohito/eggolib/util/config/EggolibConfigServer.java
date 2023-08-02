package io.github.eggohito.eggolib.util.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "server")
public class EggolibConfigServer implements ConfigData {

	@ConfigEntry.Gui.Tooltip
	public boolean performVersionCheck = true;

}
