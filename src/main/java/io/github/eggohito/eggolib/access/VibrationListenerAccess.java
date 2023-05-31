package io.github.eggohito.eggolib.access;

import net.minecraft.world.World;
import net.minecraft.world.event.PositionSource;

public interface VibrationListenerAccess {

	boolean shouldShowParticle();

	void showParticle(boolean bl);

	void tickWithPositionSource(World world, PositionSource positionSource);

}
