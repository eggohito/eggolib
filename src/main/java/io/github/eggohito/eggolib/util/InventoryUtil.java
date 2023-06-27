package io.github.eggohito.eggolib.util;

import com.google.common.collect.Sets;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.mixin.ItemSlotArgumentTypeAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryUtil {

	public static void modifyInventory(SerializableData.Instance data, Entity entity, InventoryPower inventoryPower, Function<ItemStack, Integer> processor, int limit) {

		if (limit <= 0) {
			limit = Integer.MAX_VALUE;
		}

		Consumer<Pair<World, ItemStack>> itemAction = data.get("item_action");
		Predicate<ItemStack> itemCondition = data.get("item_condition");
		Consumer<Entity> entityAction = data.get("entity_action");

		Set<Integer> slots = getSlots(data);
		deduplicateSlots(entity, slots);

		int counter = 0;

		if (inventoryPower == null) {
			processInv:
			for (int slot : slots) {

				StackReference stackReference = entity.getStackReference(slot);
				if (stackReference == StackReference.EMPTY) {
					continue;
				}

				ItemStack stack = stackReference.get();
				if (stack.isEmpty() || !(itemCondition == null || itemCondition.test(stack))) {
					continue;
				}

				if (entityAction != null) {
					entityAction.accept(entity);
				}

				int amount = processor.apply(stack);
				for (int i = 0; i < amount; i++) {
					itemAction.accept(new Pair<>(entity.world, stack));
					if (++counter >= limit) {
						break processInv;
					}
				}

			}
		} else {

			slots.removeIf(slot -> slot < 0 || slot >= inventoryPower.size());

			processInvPower:
			for (int slot : slots) {

				ItemStack stack = inventoryPower.getStack(slot);
				if (stack.isEmpty() || !(itemCondition == null || itemCondition.test(stack))) {
					continue;
				}

				if (entityAction != null) {
					entityAction.accept(entity);
				}

				int amount = processor.apply(stack);
				for (int i = 0; i < amount; i++) {
					itemAction.accept(new Pair<>(entity.world, stack));
					if (++counter >= limit) {
						break processInvPower;
					}
				}

			}

		}

	}

	public static int checkInventory(SerializableData.Instance data, Entity entity, InventoryPower inventoryPower, Function<ItemStack, Integer> processor) {

		Predicate<ItemStack> itemCondition = data.get("item_condition");
		Set<Integer> slots = getSlots(data);
		deduplicateSlots(entity, slots);
		int matches = 0;

		if (inventoryPower == null) {
			for (int slot : slots) {

				StackReference stackReference = entity.getStackReference(slot);
				if (stackReference == StackReference.EMPTY) {
					continue;
				}

				ItemStack stack = stackReference.get();
				matches += applyProcessor(stack, itemCondition, processor);

			}
		} else {
			slots.removeIf(slot -> slot < 0 || slot >= inventoryPower.size());
			for (int slot : slots) {
				ItemStack stack = inventoryPower.getStack(slot);
				matches += applyProcessor(stack, itemCondition, processor);
			}
		}

		return matches;

	}

	private static int applyProcessor(ItemStack stack, Predicate<ItemStack> itemCondition, Function<ItemStack, Integer> processor) {
		if ((itemCondition == null && !stack.isEmpty()) || (itemCondition == null || itemCondition.test(stack))) {
			return processor.apply(stack);
		} else {
			return 0;
		}
	}

	public static Set<Integer> getSlots(SerializableData.Instance data) {

		Set<Integer> slots = new HashSet<>();

		data.<ArgumentWrapper<Integer>>ifPresent("slot", w -> slots.add(w.get()));
		data.<List<ArgumentWrapper<Integer>>>ifPresent("slots", wl -> slots.addAll(wl.stream().map(ArgumentWrapper::get).toList()));

		if (slots.isEmpty()) {
			slots.addAll(ItemSlotArgumentTypeAccessor.getSlotMappings().values());
		}

		return slots;

	}

	public static void deduplicateSlots(Entity entity, Set<Integer> slots) {

		if (!(entity instanceof PlayerEntity playerEntity)) {
			return;
		}

		Map<String, Integer> slotMappings = ItemSlotArgumentTypeAccessor.getSlotMappings();

		int selectedSlot = playerEntity.getInventory().selectedSlot;
		Integer hotbarSlot = slotMappings.get("hotbar." + selectedSlot);

		if (slots.contains(hotbarSlot)) {
			Integer mainhandSlot = slotMappings.get("weapon.mainhand");
			slots.remove(mainhandSlot);
		}

	}

	public static void forEachStack(Entity entity, Consumer<ItemStack> stackConsumer) {

		Set<Integer> slots = Sets.newHashSet(ItemSlotArgumentTypeAccessor.getSlotMappings().values());
		deduplicateSlots(entity, slots);

		for (int slot : slots) {

			StackReference stackReference = entity.getStackReference(slot);
			if (stackReference == StackReference.EMPTY) {
				continue;
			}

			ItemStack stack = stackReference.get();
			if (!stack.isEmpty()) {
				stackConsumer.accept(stack);
			}

		}

		PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
		if (component == null) {
			return;
		}

		for (InventoryPower inventoryPower : component.getPowers(InventoryPower.class)) {
			for (int i = 0; i < inventoryPower.size(); i++) {
				ItemStack stack = inventoryPower.getStack(i);
				if (!stack.isEmpty()) {
					stackConsumer.accept(stack);
				}
			}
		}

	}

}
