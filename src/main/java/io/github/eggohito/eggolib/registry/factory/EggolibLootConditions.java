package io.github.eggohito.eggolib.registry.factory;

import io.github.eggohito.eggolib.loot.condition.HasTagLootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class EggolibLootConditions {

    public static void register() {
        register(HasTagLootCondition.getIdAndType());
    }

    private static void register(Pair<Identifier, LootConditionType> idAndType) {
        Registry.register(Registries.LOOT_CONDITION_TYPE, idAndType.getLeft(), idAndType.getRight());
    }

}
