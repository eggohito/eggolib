package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EntityOffset;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class ExposedToWeatherCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        BlockPos blockPos = EntityOffset.EYES.getBlockPos(entity);
        Biome.Precipitation precipitation = data.get("weather");
        Boolean thundering = data.get("thundering");

        return exposedToWeather(entity.world, blockPos, precipitation)
            && (thundering == null || (thundering && entity.world.isThundering()));

    }

    private static boolean exposedToWeather(World world, BlockPos blockPos, Biome.Precipitation precipitation) {
        return world.isRaining()
            && world.isSkyVisible(blockPos)
            && world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, blockPos).getY() < blockPos.getY()
            && (precipitation == null || world.getBiome(blockPos).value().getPrecipitation() == precipitation);
    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("exposed_to_weather"),
            new SerializableData()
                .add("weather", SerializableDataType.enumValue(Biome.Precipitation.class), null)
                .add("thundering", SerializableDataTypes.BOOLEAN, null),
            ExposedToWeatherCondition::condition
        );
    }

}
