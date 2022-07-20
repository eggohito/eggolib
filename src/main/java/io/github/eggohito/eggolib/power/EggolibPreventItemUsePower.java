package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.ActiveInteractionPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EggolibPreventItemUsePower extends ActiveInteractionPower {

    private final Consumer<Entity> entityAction;

    public EggolibPreventItemUsePower(PowerType<?> type, LivingEntity entity, EnumSet<Hand> hands, ActionResult actionResult, Predicate<ItemStack> itemCondition, Consumer<Pair<World, ItemStack>> heldItemAction, ItemStack itemResult, Consumer<Pair<World, ItemStack>> resultItemAction, int priority, Consumer<Entity> entityAction) {
        super(type, entity, hands, actionResult, itemCondition, heldItemAction, itemResult, resultItemAction, priority);
        this.entityAction = entityAction;
    }

    public void executeActions(Hand hand) {
        if (entityAction != null) entityAction.accept(entity);
        performActorItemStuff(this, (PlayerEntity) entity, hand);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("prevent_item_use"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("held_item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("result_item_action", ApoliDataTypes.ITEM_ACTION, null)
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("result_stack", SerializableDataTypes.ITEM_STACK, null)
                .add("hands", SerializableDataTypes.HAND_SET, EnumSet.allOf(Hand.class))
                .add("priority", SerializableDataTypes.INT, 0),
            data -> (powerType, livingEntity) -> new EggolibPreventItemUsePower(
                powerType,
                livingEntity,
                data.get("hands"),
                ActionResult.FAIL,
                data.get("item_condition"),
                data.get("held_item_action"),
                data.get("result_stack"),
                data.get("result_item_action"),
                data.getInt("priority"),
                data.get("entity_action")
            )
        );
    }

}
