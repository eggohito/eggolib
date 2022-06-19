package io.github.eggohito.eggolib.util;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "server")
public class EggolibConfigServer implements ConfigData {

    public boolean performVersionCheck = true;

}
