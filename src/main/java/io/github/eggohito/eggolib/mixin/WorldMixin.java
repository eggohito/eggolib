package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.access.WeatherView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(World.class)
public abstract class WorldMixin implements WorldAccess, AutoCloseable, WeatherView {

	@Shadow
	public abstract boolean isRaining();

	@Override
	public boolean hasSnow(BlockPos... blockPosList) {
		return Arrays.stream(blockPosList)
			.anyMatch(this::eggolib$hasSnow);
	}

	@Unique
	private boolean eggolib$hasSnow(BlockPos pos) {
		Biome biome = this.getBiome(pos).value();
		return this.isRaining()
			&& this.isSkyVisible(pos)
			&& this.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() < pos.getY()
			&& biome.getPrecipitation(pos) == Biome.Precipitation.SNOW;
	}

}
