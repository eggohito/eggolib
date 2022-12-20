package io.github.eggohito.eggolib.integration;

import io.github.eggohito.eggolib.power.ActionOnBlockHitPower;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

public class EggolibPowerIntegration {

    public static void register() {
        AttackBlockCallback.EVENT.register(ActionOnBlockHitPower::integrateCallback);
    }

}
