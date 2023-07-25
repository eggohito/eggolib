package io.github.eggohito.eggolib.util;

import io.github.eggohito.eggolib.access.LinkableListenerData;
import io.github.eggohito.eggolib.power.GameEventListenerPower;
import net.minecraft.world.event.Vibrations;

public final class VibrationsTickerUtil {

	private static Vibrations.ListenerData cachedListenerData;

	public static void cache(Vibrations.ListenerData listenerData) {
		cachedListenerData = listenerData;
	}

	public static boolean preventParticle() {
		return cachedListenerData != null && ((LinkableListenerData) cachedListenerData).eggolib$getLinkedPower()
			.map(p -> p instanceof GameEventListenerPower gelp && !gelp.shouldShowParticle())
			.orElse(false);
	}

}
