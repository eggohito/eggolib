package io.github.eggohito.eggolib.access;

import net.minecraft.util.math.BlockPos;

public interface WeatherView {
	boolean hasSnow(BlockPos... pos);
}
