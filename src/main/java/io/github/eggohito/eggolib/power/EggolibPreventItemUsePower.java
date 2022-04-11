package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PreventItemUsePower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class EggolibPreventItemUsePower extends PreventItemUsePower {

    private final Predicate<ItemStack> itemCondition;
    private final boolean includeBlockItems;

    public EggolibPreventItemUsePower(PowerType<?> powerType, LivingEntity livingEntity, Predicate<ItemStack> itemCondition, boolean includeBlockItems) {
        super(powerType, livingEntity, itemCondition);
        this.itemCondition = itemCondition;
        this.includeBlockItems = includeBlockItems;
    }

    public boolean doesPreventPlacement(ItemStack itemStack) {
        return includeBlockItems && itemCondition.test(itemStack);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("prevent_item_use"),
            new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("include_block_items", SerializableDataTypes.BOOLEAN, true),
            data -> (powerType, livingEntity) -> new EggolibPreventItemUsePower(
                powerType,
                livingEntity,
                data.get("item_condition"),
                data.getBoolean("include_block_items")
            )
        ).allowCondition();
    }
}
