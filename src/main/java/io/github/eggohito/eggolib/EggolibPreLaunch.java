package io.github.eggohito.eggolib;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class EggolibPreLaunch implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        MixinExtrasBootstrap.init();
    }

}
