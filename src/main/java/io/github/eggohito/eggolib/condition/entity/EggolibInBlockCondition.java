package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.util.EntityOffset;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class EggolibInBlockCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        EntityOffset entityOffset = data.get("offset");
        BlockPos blockPos = new BlockPos(entityOffset.get(entity));
        Predicate<CachedBlockPosition> blockCondition = data.get("block_condition");

        return blockCondition.test(new CachedBlockPosition(entity.world, blockPos, true));

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("in_block"),
            new SerializableData()
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION)
                .add("offset", SerializableDataType.enumValue(EntityOffset.class), EntityOffset.FEET),
            EggolibInBlockCondition::condition
        );
    }

}
