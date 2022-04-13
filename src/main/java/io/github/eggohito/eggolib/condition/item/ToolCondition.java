package io.github.eggohito.eggolib.condition.item;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.ToolType;
import net.minecraft.item.*;

import java.util.EnumSet;

public class ToolCondition {

    public static boolean condition(SerializableData.Instance data, ItemStack itemStack) {

        int matches = 0;

        EnumSet<ToolType> toolTypes = data.get("tool_types");
        for (ToolType toolType: toolTypes) {
            switch (toolType) {
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

        return (matches > 0);
    }

    public static ConditionFactory<ItemStack> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("tool"),
            new SerializableData()
                .add("tool_types", EggolibDataTypes.TOOL_TYPE_SET, EnumSet.allOf(ToolType.class)),
            ToolCondition::condition
        );
    }
}
