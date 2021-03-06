package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.condition.item.*;
import io.github.eggohito.eggolib.condition.meta.ChanceCondition;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

public class EggolibItemConditions {

    public static void register() {
        register(BlockItemCondition.getFactory());
        register(ToolCondition.getFactory());
    }

    private static void register(ConditionFactory<ItemStack> conditionFactory) {
        Registry.register(ApoliRegistries.ITEM_CONDITION, conditionFactory.getSerializerId(), conditionFactory);
    }
}
