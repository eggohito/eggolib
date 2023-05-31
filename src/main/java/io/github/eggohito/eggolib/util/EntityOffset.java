package io.github.eggohito.eggolib.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;

public enum EntityOffset {

	FEET(entity -> new Pair<>(entity.getPos(), new Vec3d(0, 0, 0))),
	EYES(entity -> new Pair<>(entity.getPos(), new Vec3d(0, entity.getEyeHeight(entity.getPose()), 0)));

	final Function<Entity, Pair<Vec3d, Vec3d>> offsetApplier;

	EntityOffset(Function<Entity, Pair<Vec3d, Vec3d>> offsetApplier) {
		this.offsetApplier = offsetApplier;
	}

	public Vec3d getPos(Entity entity) {
		return offsetApplier.apply(entity).getLeft()
			.add(getOffset(entity).getX(), getOffset(entity).getY(), getOffset(entity).getZ());
	}

	public BlockPos getBlockPos(Entity entity) {
		return BlockPos.ofFloored(getPos(entity));
	}

	public Vec3d getOffset(Entity entity) {
		return offsetApplier.apply(entity).getRight();
	}

}
