package io.github.eggohito.eggolib.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.eggohito.eggolib.integration.autoconfig.EggolibAutoConfigIntegration;
import io.github.eggohito.eggolib.util.config.EggolibConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EggolibModMenuIntegration implements ModMenuApi {

	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> EggolibAutoConfigIntegration.getConfigScreen(EggolibConfig.class, parent).get();
	}

}
