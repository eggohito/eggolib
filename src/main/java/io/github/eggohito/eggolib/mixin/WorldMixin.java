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

	@Shadow
	public abstract boolean isThundering();

	@Override
	public boolean inSnow(BlockPos... blockPosList) {
		return Arrays.stream(blockPosList)
			.anyMatch(this::eggolib$inSnow);
	}

	@Override
	public boolean inThunderstorm(BlockPos... pos) {
		return Arrays.stream(pos)
			.anyMatch(this::eggolib$inThunderstorm);
	}

	@Unique
	private boolean eggolib$inWeather(BlockPos pos) {
		return this.isRaining()
			&& this.isSkyVisible(pos)
			&& this.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() < pos.getY();
	}

	@Unique
	private boolean eggolib$inSnow(BlockPos pos) {
		return eggolib$inWeather(pos)
			&& this.getBiome(pos).value().getPrecipitation(pos) == Biome.Precipitation.SNOW;
	}

	@Unique
	private boolean eggolib$inThunderstorm(BlockPos pos) {
		return eggolib$inWeather(pos)
			&& this.isThundering();
	}

}
