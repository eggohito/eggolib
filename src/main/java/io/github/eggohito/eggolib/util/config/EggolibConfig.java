package io.github.eggohito.eggolib.util.config;

import io.github.eggohito.eggolib.Eggolib;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Eggolib.MOD_ID)
public class EggolibConfig extends PartitioningSerializer.GlobalData {

	@ConfigEntry.Category("server")
	@ConfigEntry.Gui.TransitiveObject
	public EggolibConfigServer server = new EggolibConfigServer();

	@ConfigEntry.Category("client")
	@ConfigEntry.Gui.TransitiveObject
	public EggolibConfigClient client = new EggolibConfigClient();

}
