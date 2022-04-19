package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import io.github.eggohito.eggolib.networking.EggolibPacketsS2C;
import net.fabricmc.api.ClientModInitializer;

public class EggolibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EggolibPacketsS2C.register();
        EggolibClassDataClient.registerAll();
    }

}
