package io.github.eggohito.eggolib.util;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;

public class StackReferenceUtil implements StackReference {

	private final Entity owner;
	private final StackReference stackReference;

	private StackReferenceUtil(Entity owner, int mappedIndex) {
		this.owner = owner;
		this.stackReference = owner.getStackReference(mappedIndex);
	}

	public static StackReference of(Entity owner, int mappedIndex) {
		StackReferenceUtil util = new StackReferenceUtil(owner, mappedIndex);
		return util.stackReference != EMPTY ? util : EMPTY;
	}

	@Override
	public ItemStack get() {

		ItemStack stack = stackReference.get();
		stack.setHolder(owner);

		return stack;

	}

	@Override
	public boolean set(ItemStack stack) {
		stack.setHolder(owner);
		return stackReference.set(stack);
	}

}
