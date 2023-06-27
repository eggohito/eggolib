package io.github.eggohito.eggolib.util;

import net.minecraft.item.ItemStack;

import java.util.function.Function;

public enum ProcessMode {

	STACKS(stack -> 1),
	ITEMS(ItemStack::getCount);

	private final Function<ItemStack, Integer> processor;
	ProcessMode(Function<ItemStack, Integer> processor) {
		this.processor = processor;
	}

	public Function<ItemStack, Integer> getProcessor() {
		return processor;
	}

}
