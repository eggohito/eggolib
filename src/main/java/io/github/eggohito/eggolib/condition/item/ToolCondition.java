package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.EggolibToolType;
import net.minecraft.item.*;

import java.util.EnumSet;

public class ToolCondition {

    public static boolean condition(SerializableData.Instance data, ItemStack itemStack) {

        EnumSet<EggolibToolType> eggolibToolTypes = EnumSet.noneOf(EggolibToolType.class);

        if (data.isPresent("tool_type") || data.isPresent("tool_types")) {
            if (data.isPresent("tool_type")) eggolibToolTypes.add(data.get("tool_type"));
            if (data.isPresent("tool_types")) eggolibToolTypes.addAll(data.get("tool_types"));
        }

        else {
            eggolibToolTypes.addAll(EnumSet.allOf(EggolibToolType.class));
        }

        return (itemToolTypeMatches(eggolibToolTypes, itemStack) > 0);
    }

    private static int itemToolTypeMatches(EnumSet<EggolibToolType> eggolibToolTypes, ItemStack itemStack) {

        int matches = 0;

        for (EggolibToolType tt : eggolibToolTypes) {
            switch (tt) {
                case AXE:
                    if (itemStack.getItem() instanceof AxeItem) matches++;
                    break;
                case HOE:
                    if (itemStack.getItem() instanceof HoeItem) matches++;
                    break;
                case PICKAXE:
                    if (itemStack.getItem() instanceof PickaxeItem) matches++;
                    break;
                case SHOVEL:
                    if (itemStack.getItem() instanceof ShovelItem) matches++;
                    break;
                case SWORD:
                    if (itemStack.getItem() instanceof SwordItem) matches++;
                    break;
                case SHEARS:
                    if (itemStack.getItem() instanceof ShearsItem) matches++;
                    break;
            }
        }

        return matches;
    }

    public static ConditionFactory<ItemStack> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("tool"),
            new SerializableData()
                .add("tool_type", EggolibDataTypes.TOOL_TYPE, null)
                .add("tool_types", EggolibDataTypes.TOOL_TYPE_SET, null),
            ToolCondition::condition
        );
    }
}
