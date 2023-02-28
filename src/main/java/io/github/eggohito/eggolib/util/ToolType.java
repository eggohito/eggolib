package io.github.eggohito.eggolib.util;

import net.minecraft.item.*;

import java.util.function.Function;

public enum ToolType {

    AXE(item -> item instanceof AxeItem),
    HOE(item -> item instanceof HoeItem),
    PICKAXE(item -> item instanceof PickaxeItem),
    SHOVEL(item -> item instanceof ShovelItem),
    SWORD(item -> item instanceof SwordItem),
    SHEARS(item -> item instanceof ShearsItem);

    private final Function<Item, Boolean> function;
    ToolType(Function<Item, Boolean> function) {
        this.function = function;
    }

    public boolean matches(Item item) {
        return this.function.apply(item);
    }

    public boolean matches(ItemStack stack) {
        return this.function.apply(stack.getItem());
    }

}
