package io.github.eggohito.eggolib.integration;

import dev.micalobia.breathinglib.event.BreathingCallback;
import io.github.eggohito.eggolib.power.ActionOnBlockHitPower;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.power.ModifyBreathingPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;

public class EggolibPowerIntegration {

	public static void register() {
		AttackBlockCallback.EVENT.register(ActionOnBlockHitPower::integrateCallback);
		BreathingCallback.EVENT.register(ModifyBreathingPower::integrateCallback);
	}

	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		ClientTickEvents.START_CLIENT_TICK.register(ActionOnKeySequencePower::integrateCallback);
	}

}
