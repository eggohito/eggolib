package io.github.eggohito.eggolib.registry.factory;

import io.github.eggohito.eggolib.loot.condition.InGroupLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EggolibLootConditions {

    public static void register() {
        register(InGroupLootCondition.getIdAndType());
    }

    private static void register(Pair<Identifier, LootConditionType> idAndType) {
        Registry.register(Registry.LOOT_CONDITION_TYPE, idAndType.getLeft(), idAndType.getRight());
    }

}
