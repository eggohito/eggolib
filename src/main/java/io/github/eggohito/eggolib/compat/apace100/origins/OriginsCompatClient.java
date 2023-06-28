package io.github.eggohito.eggolib.compat.apace100.origins;

import io.github.apace100.origins.screen.ChooseOriginScreen;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.compat.EggolibModCompatClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

@Environment(EnvType.CLIENT)
public class OriginsCompatClient extends EggolibModCompatClient {

	@Override
	public void init() {

		ModContainer origins = FabricLoader.getInstance().getModContainer("origins").orElse(null);
		if (origins == null) {
			return;
		}

		addScreenMapping("choose_origin", ChooseOriginScreen.class);
		addScreenMapping("view_origin", ViewOriginScreen.class);

		Eggolib.LOGGER.warn(
			"[{}] Detected {} by {}! Successfully registered these classes to the client class data registry: \n{}",
			Eggolib.MOD_ID,
			getModName(origins),
			getModAuthors(origins),
			formatScreenMappings()
		);

	}

}
