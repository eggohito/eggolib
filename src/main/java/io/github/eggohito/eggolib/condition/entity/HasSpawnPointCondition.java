package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HasSpawnPointCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		MinecraftServer server = entity.getServer();
		if (server == null || !(entity instanceof ServerPlayerEntity serverPlayerEntity)) {
			return false;
		}

		RegistryKey<World> spawnPointDimension = serverPlayerEntity.getSpawnPointDimension();
		BlockPos spawnPointPos = serverPlayerEntity.getSpawnPointPosition();
		ServerWorld world = server.getWorld(spawnPointDimension);
		if (world == null) {
			return false;
		}

		BlockState state = world.getBlockState(spawnPointPos);
		return state.isIn(BlockTags.BEDS)
			|| state.isOf(Blocks.RESPAWN_ANCHOR);

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("has_spawn_point"),
			new SerializableData(),
			HasSpawnPointCondition::condition
		);
	}

}
