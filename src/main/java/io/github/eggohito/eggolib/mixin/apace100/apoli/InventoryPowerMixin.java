package io.github.eggohito.eggolib.mixin.apace100.apoli;

import io.github.apace100.apoli.power.Active;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(InventoryPower.class)
public abstract class InventoryPowerMixin extends Power implements Active, Inventory {

	@Shadow
	@Final
	private DefaultedList<ItemStack> container;

	public InventoryPowerMixin(PowerType<?> type, LivingEntity entity) {
		super(type, entity);
	}

	/**
	 * @author eggohito
	 * @reason To get the stack with its holder
	 */
	@Overwrite
	public ItemStack getStack(int slot) {

		ItemStack stack = container.get(slot);
		stack.setHolder(entity);

		return stack;

	}

	/**
	 * @author eggohito
	 * @reason To set the stack with its holder in the inventory
	 */
	@Overwrite
	public void setStack(int slot, ItemStack stack) {
		stack.setHolder(entity);
		container.set(slot, stack);
	}

	/**
	 * @author eggohito
	 * @reason To split the stack with its holder
	 */
	@Overwrite
	public ItemStack removeStack(int slot, int amount) {
		return getStack(slot).split(amount);
	}

	/**
	 * @author eggohito
	 * @reason To get the stack that was removed with its holder
	 */
	@Overwrite
	public ItemStack removeStack(int slot) {

		ItemStack stack = getStack(slot);
		setStack(slot, ItemStack.EMPTY);

		return stack;

	}

}
