package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.function.Predicate;

public class EggolibBlockCollisionCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        Predicate<CachedBlockPosition> blockCondition = data.get("block_condition");
        Box entityBoundingBox = entity.getBoundingBox();
        Box offsetEntityBoundingBox = entityBoundingBox.offset(
            data.getFloat("offset_x") * entityBoundingBox.getXLength(),
            data.getFloat("offset_y") * entityBoundingBox.getYLength(),
            data.getFloat("offset_z") * entityBoundingBox.getZLength()
        );

        if (blockCondition != null) {

            int matchingBlocks = 0;
            BlockPos minBlockPos = new BlockPos(offsetEntityBoundingBox.minX + 0.001, offsetEntityBoundingBox.minY + 0.001, offsetEntityBoundingBox.minZ + 0.001);
            BlockPos maxBlockPos = new BlockPos(offsetEntityBoundingBox.maxX - 0.001, offsetEntityBoundingBox.maxY - 0.001, offsetEntityBoundingBox.maxZ - 0.001);
            BlockPos.Mutable mutBlockPos = new BlockPos.Mutable();

            for (int i = minBlockPos.getX(); i <= maxBlockPos.getX(); i++) {
                for (int j = minBlockPos.getY(); j <= maxBlockPos.getY(); j++) {
                    for (int k = minBlockPos.getZ(); k <= maxBlockPos.getZ(); k++) {
                        mutBlockPos.set(i, j, k);
                        if (blockCondition.test(new CachedBlockPosition(entity.world, mutBlockPos, true))) matchingBlocks++;
                    }
                }
            }

            return matchingBlocks > 0;

        }

        else return entity.world
            .getBlockCollisions(entity, offsetEntityBoundingBox)
            .iterator()
            .hasNext();

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("block_collision"),
            new SerializableData()
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                .add("offset_x", SerializableDataTypes.FLOAT, 0F)
                .add("offset_y", SerializableDataTypes.FLOAT, 0F)
                .add("offset_z", SerializableDataTypes.FLOAT, 0F),
            EggolibBlockCollisionCondition::condition
        );
    }

}
