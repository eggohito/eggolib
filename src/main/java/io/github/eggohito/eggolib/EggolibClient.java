package io.github.eggohito.eggolib;

import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import net.fabricmc.api.ClientModInitializer;

public class EggolibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EggolibClassDataClient.registerAll();
    }
}
