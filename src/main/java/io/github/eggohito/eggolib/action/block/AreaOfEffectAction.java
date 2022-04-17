package io.github.eggohito.eggolib.action.block;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.Shape;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class AreaOfEffectAction {

    public static void action(SerializableData.Instance data, Triple<World, BlockPos, Direction> block) {

        World world = block.getLeft();
        BlockPos blockPos = block.getMiddle();
        Direction direction = block.getRight();

        Shape shape = data.get("shape");
        int radius = data.getInt("radius");
        Consumer<Triple<World, BlockPos, Direction>> blockAction = data.get("block_action");

        Predicate<CachedBlockPosition> blockCondition = null;
        if (data.isPresent("block_condition")) blockCondition = data.get("block_condition");

        for (BlockPos collectedBlockPos : Shape.getPositions(blockPos, shape, radius)) {
            if (blockCondition == null || blockCondition.test(new CachedBlockPosition(world, collectedBlockPos, true))) {
                blockAction.accept(Triple.of(world, collectedBlockPos, direction));
            }
        }
    }

    public static ActionFactory<Triple<World, BlockPos, Direction>> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("area_of_effect"),
            new SerializableData()
                .add("radius", SerializableDataTypes.INT)
                .add("shape", SerializableDataType.enumValue(Shape.class), Shape.CUBE)
                .add("block_action", ApoliDataTypes.BLOCK_ACTION)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null),
            AreaOfEffectAction::action
        );
    }
}
