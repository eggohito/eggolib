package io.github.eggohito.eggolib.util;

import io.github.eggohito.eggolib.Eggolib;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = Eggolib.MOD_ID)
public class EggolibConfig extends PartitioningSerializer.GlobalData {

    @ConfigEntry.Category("server")
    @ConfigEntry.Gui.TransitiveObject
    public EggolibConfigServer server = new EggolibConfigServer();

}
