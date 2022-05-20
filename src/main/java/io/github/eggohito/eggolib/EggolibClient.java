package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.compat.EggolibOriginsCompat;
import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import io.github.eggohito.eggolib.networking.EggolibPacketsS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class EggolibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        EggolibPacketsS2C.register();
        EggolibClassDataClient.register();

        //  Add Origins' screen classes to Eggolib's screen class data registry if it's loaded
        FabricLoader.getInstance().getModContainer("origins").ifPresent(EggolibOriginsCompat::init);

    }

}
