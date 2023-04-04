package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.item.*;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registry;

public class EggolibItemConditions {

    public static void register() {
        register(BlockItemCondition.getFactory());
        register(EggolibEnchantmentCondition.getFactory());
        register(ToolCondition.getFactory());
    }

    public static ConditionFactory<ItemStack> register(ConditionFactory<ItemStack> conditionFactory) {
        return Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }

}
