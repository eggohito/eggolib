package io.github.eggohito.eggolib.integration;

import dev.micalobia.breathinglib.event.BreathingCallback;
import io.github.eggohito.eggolib.power.ActionOnBlockHitPower;
import io.github.eggohito.eggolib.power.ModifyBreathingPower;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

public class EggolibPowerIntegration {

    public static void register() {
        AttackBlockCallback.EVENT.register(ActionOnBlockHitPower::integrateCallback);
        BreathingCallback.EVENT.register(ModifyBreathingPower::integrateCallback);
    }

}
