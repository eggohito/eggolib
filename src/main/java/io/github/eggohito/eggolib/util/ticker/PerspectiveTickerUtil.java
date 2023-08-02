package io.github.eggohito.eggolib.util.ticker;

import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.Perspective;

@Environment(EnvType.CLIENT)
public class PerspectiveTickerUtil extends TickerUtil<Perspective> {

	@Override
	public void execute() {
		MiscUtilClient.getPerspective(MinecraftClient.getInstance(), dataWrapper.wrappedData());
	}

}
