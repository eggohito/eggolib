package io.github.eggohito.eggolib.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.eggohito.eggolib.util.config.EggolibConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class EggolibModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> AutoConfig.getConfigScreen(EggolibConfig.class, parent).get();
	}

}
