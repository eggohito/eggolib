package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.mixin.ItemSlotArgumentTypeAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InventoryUtil {

	public static int checkInventory(SerializableData.Instance data, Entity entity, InventoryPower inventoryPower) {

		Set<Integer> slots = getSlots(data);
		Predicate<ItemStack> itemCondition = data.get("item_condition");

		int matches = 0;

		if (inventoryPower == null) {
			for (int slot : slots) {

				StackReference stackReference = entity.getStackReference(slot);
				if (stackReference == StackReference.EMPTY) {
					continue;
				}

				ItemStack stack = stackReference.get();
				if ((itemCondition == null && !stack.isEmpty()) || (itemCondition == null || itemCondition.test(stack))) {
					matches++;
				}

			}
		} else {
			for (int slot : slots) {

				if (slot < 0 || slot >= inventoryPower.size()) {
					continue;
				}

				ItemStack stack = inventoryPower.getStack(slot);
				if ((itemCondition == null && !stack.isEmpty()) || (itemCondition == null || itemCondition.test(stack))) {
					matches++;
				}

			}
		}

		return matches;

	}

	public static Set<Integer> getSlots(SerializableData.Instance data) {

		Set<Integer> itemSlots = new HashSet<>();

		data.<ArgumentWrapper<Integer>>ifPresent("slot", iaw -> itemSlots.add(iaw.get()));
		data.<List<ArgumentWrapper<Integer>>>ifPresent("slots", liaw -> itemSlots.addAll(liaw.stream().map(ArgumentWrapper::get).toList()));

		if (itemSlots.isEmpty()) {
			itemSlots.addAll(ItemSlotArgumentTypeAccessor.getSlotMappings().values());
		}
		return itemSlots;

	}

}
