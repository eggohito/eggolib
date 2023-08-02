package io.github.eggohito.eggolib.util.ticker;

import io.github.eggohito.eggolib.util.MiscUtilClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class ScreenTickerUtil extends TickerUtil<Screen> {

	@Override
	public void execute() {
		MiscUtilClient.getScreenState(MinecraftClient.getInstance(), dataWrapper.wrappedData());
	}

}
