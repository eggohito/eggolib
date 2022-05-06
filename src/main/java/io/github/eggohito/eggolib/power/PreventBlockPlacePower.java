package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.InteractionPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PreventBlockPlacePower extends InteractionPower {

    private final Consumer<Entity> entityAction;
    private final EnumSet<Direction> directions;
    private final Predicate<CachedBlockPosition> blockCondition;

    public PreventBlockPlacePower(PowerType<?> powerType, LivingEntity livingEntity, EnumSet<Hand> hands, ActionResult actionResult, Predicate<ItemStack> itemCondition, Consumer<Pair<World, ItemStack>> heldItemAction, ItemStack resultItem, Consumer<Pair<World, ItemStack>> resultItemAction, Predicate<CachedBlockPosition> blockCondition, EnumSet<Direction> directions, Consumer<Entity> entityAction) {
        super(powerType, livingEntity, hands, actionResult, itemCondition, heldItemAction, resultItem, resultItemAction);
        this.blockCondition = blockCondition;
        this.directions = directions;
        this.entityAction = entityAction;
    }

    public boolean doesApply(BlockPos blockPos, Direction direction, ItemStack itemStack, Hand hand) {
        if (!shouldExecute(hand, itemStack)) return false;
        if (!directions.contains(direction)) return false;
        return blockCondition == null || blockCondition.test(new CachedBlockPosition(entity.world, blockPos, true));
    }

    public boolean executeAction(Hand hand) {
        if (entityAction != null) entityAction.accept(entity);
        performActorItemStuff(this, (PlayerEntity) entity, hand);
        return false;
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("prevent_block_place"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                .add("directions", SerializableDataTypes.DIRECTION_SET, EnumSet.allOf(Direction.class))
                .add("hands", SerializableDataTypes.HAND_SET, EnumSet.allOf(Hand.class))
                .add("result_stack", SerializableDataTypes.ITEM_STACK, null)
                .add("held_item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("result_item_action", ApoliDataTypes.ITEM_ACTION, null),
            data -> (powerType, livingEntity) -> new PreventBlockPlacePower(
                powerType,
                livingEntity,
                data.get("hands"),
                ActionResult.FAIL,
                data.get("item_condition"),
                data.get("held_item_action"),
                data.get("result_stack"),
                data.get("result_item_action"),
                data.get("block_condition"),
                data.get("directions"),
                data.get("entity_action")
            )
        ).allowCondition();
    }

}
