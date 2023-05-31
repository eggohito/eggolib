package io.github.eggohito.eggolib.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Optional;

public class MiscUtilServer {

	public static Optional<Entity> getEntityWithPassengers(ServerWorld serverWorld, EntityType<?> entityType, Vec3d pos, float yaw, float pitch) {
		return getEntityWithPassengers(serverWorld, entityType, null, pos, yaw, pitch);
	}

	public static Optional<Entity> getEntityWithPassengers(ServerWorld serverWorld, EntityType<?> entityType, @Nullable NbtCompound entityNbt, Vec3d pos, float yaw, float pitch) {

		NbtCompound entityToSummonNbt = new NbtCompound();
		if (entityNbt != null) {
			entityToSummonNbt.copyFrom(entityNbt);
		}
		entityToSummonNbt.putString("id", Registries.ENTITY_TYPE.getId(entityType).toString());

		Entity entityToSummon = EntityType.loadEntityWithPassengers(
			entityToSummonNbt,
			serverWorld,
			loadedEntity -> {
				loadedEntity.refreshPositionAndAngles(pos.x, pos.y, pos.z, yaw, pitch);
				return loadedEntity;
			}
		);

		if (entityToSummon == null) {
			return Optional.empty();
		}

		if (entityNbt == null && entityToSummon instanceof MobEntity mobToSummon) {
			mobToSummon.initialize(
				serverWorld,
				serverWorld.getLocalDifficulty(mobToSummon.getBlockPos()),
				SpawnReason.MOB_SUMMONED,
				null,
				null
			);
		}

		return Optional.of(entityToSummon);

	}

}
