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
			.anyMatch(
				p -> eggolib$isRainingAndExposed(p)
					&& this.getBiome(p).value().getPrecipitation(p) == Biome.Precipitation.SNOW
			);
	}

	@Override
	public boolean inThunderstorm(BlockPos... pos) {
		return Arrays.stream(pos)
			.anyMatch(
				p -> eggolib$isRainingAndExposed(p)
				&& this.isThundering()
			);
	}

	@Unique
	private boolean eggolib$isRainingAndExposed(BlockPos pos) {
		return this.isRaining()
			&& this.isSkyVisible(pos)
			&& this.getTopPosition(Heightmap.Type.MOTION_BLOCKING, pos).getY() < pos.getY();
	}

}
