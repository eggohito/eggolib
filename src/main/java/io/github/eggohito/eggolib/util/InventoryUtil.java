package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.mixin.ItemSlotArgumentTypeAccessor;
import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.ArgumentWrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class InventoryUtil {

	public static Set<Integer> getSlots(SerializableData.Instance data) {

		Set<Integer> slots = new HashSet<>();

		data.<ArgumentWrapper<Integer>>ifPresent("slot", iaw -> slots.add(iaw.get()));
		data.<List<ArgumentWrapper<Integer>>>ifPresent("slots", iaws -> slots.addAll(iaws.stream().map(ArgumentWrapper::get).toList()));

		if (slots.isEmpty()) slots.addAll(ItemSlotArgumentTypeAccessor.getSlotMappings().values());

		return slots;

	}

	public static int checkInventory(SerializableData.Instance data, Entity entity, @Nullable InventoryPower inventoryPower, Function<ItemStack, Integer> processor) {

		Predicate<Pair<World, ItemStack>> itemCondition = data.get("item_condition");
		Set<Integer> slots = getSlots(data);
		deduplicateSlots(entity, slots);

		int matches = 0;
		slots.removeIf(slot -> slotNotWithinBounds(entity, inventoryPower, slot));
		for (int slot : slots) {

			ItemStack stack = getStack(entity, inventoryPower, slot);
			if ((itemCondition == null && !stack.isEmpty()) || (itemCondition == null || itemCondition.test(new Pair<>(entity.getWorld(), stack)))) {
				matches += processor.apply(stack);
			}

		}

		return matches;

	}

	public static void modifyInventory(SerializableData.Instance data, Entity entity, InventoryPower inventoryPower, Function<ItemStack, Integer> processor, int limit) {

		if(limit <= 0) {
			limit = Integer.MAX_VALUE;
		}

		Set<Integer> slots = getSlots(data);
		deduplicateSlots(entity, slots);

		Consumer<Entity> entityAction = data.get("entity_action");
		Predicate<Pair<World, ItemStack>> itemCondition = data.get("item_condition");
		ActionFactory<Pair<World, ItemStack>>.Instance itemAction = data.get("item_action");

		int processedItems = 0;
		slots.removeIf(slot -> slotNotWithinBounds(entity, inventoryPower, slot));

		modifyingItemsLoop:
		for (int slot : slots) {

			ItemStack stack = getStack(entity, inventoryPower, slot);
			if (stack.isEmpty() || !(itemCondition == null || itemCondition.test(new Pair<>(entity.getWorld(), stack)))) {
				continue;
			}

			int amount = processor.apply(stack);
			for (int i = 0; i < amount; i++) {

				if (entityAction != null) {
					entityAction.accept(entity);
				}

				itemAction.accept(new Pair<>(entity.getWorld(), stack));
				++processedItems;

				if (processedItems >= limit) {
					break modifyingItemsLoop;
				}

			}

		}

	}

	public static void forEachStack(Entity entity, Consumer<ItemStack> stackConsumer) {

		int slotToSkip = getDuplicatedSlotIndex(entity);
		for (int slot : ItemSlotArgumentTypeAccessor.getSlotMappings().values()) {

			if (slot == slotToSkip) {
				slotToSkip = Integer.MIN_VALUE;
				continue;
			}

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

		List<InventoryPower> inventoryPowers = component.getPowers(InventoryPower.class);
		for (InventoryPower inventoryPower : inventoryPowers) {
			for (int index = 0; index < inventoryPower.size(); index++) {

				ItemStack stack = inventoryPower.getStack(index);

				if (!stack.isEmpty()) {
					stackConsumer.accept(stack);
				}

			}
		}

	}

	private static void deduplicateSlots(Entity entity, Set<Integer> slots) {

		int hotbarSlot = getDuplicatedSlotIndex(entity);

		if (hotbarSlot != Integer.MIN_VALUE && slots.contains(hotbarSlot)) {
			slots.remove(ItemSlotArgumentTypeAccessor.getSlotMappings().get("weapon.mainhand"));
		}

	}

	/**
	 *      <p>For players, their selected hotbar slot will overlap with the `weapon.mainhand` slot reference. This
	 *      method returns the slot ID of the selected hotbar slot.</p>
	 *
	 *      @param entity   The entity to get the slot ID of its selected hotbar slot
	 *      @return         The slot ID of the hotbar slot or {@link Integer#MIN_VALUE} if the entity is not a player
	 */
	private static int getDuplicatedSlotIndex(Entity entity) {

		if (entity instanceof PlayerEntity player) {
			return ItemSlotArgumentTypeAccessor.getSlotMappings().get("hotbar." + player.getInventory().selectedSlot);
		}

		return Integer.MIN_VALUE;

	}

	/**
	 *      <p>Check whether the specified slot is <b>not</b> within the bounds of the entity's {@linkplain
	 *      StackReference stack reference} or the specified {@link InventoryPower}.</p>
	 *
	 *      @param entity           The entity check the bounds of its {@linkplain StackReference stack reference}
	 *      @param inventoryPower   The {@link InventoryPower} to check the bounds of
	 *      @param slot             The slot
	 *      @return                 {@code true} if the slot is within the bounds of the {@linkplain
	 *      StackReference stack reference} or the {@link InventoryPower}
	 */
	public static boolean slotNotWithinBounds(Entity entity, @Nullable InventoryPower inventoryPower, int slot) {
		return inventoryPower == null ? entity.getStackReference(slot) == StackReference.EMPTY
		                              : slot < 0 || slot >= inventoryPower.size();
	}

	/**
	 *      <p>Get the item stack from the entity's {@linkplain StackReference stack reference} or the inventory of
	 *      the specified {@link InventoryPower} (if it's not null).</p>
	 *
	 *      <p><b>Make sure to only call this method after you filter out the slots that aren't within the bounds
	 *      of the entity's {@linkplain StackReference stack reference} or {@link InventoryPower} using {@link
	 *      #slotNotWithinBounds(Entity, InventoryPower, int)}</b></p>
	 *
	 *      @param entity            The entity to get the item stack from its {@linkplain StackReference stack reference}
	 *      @param inventoryPower    The {@link InventoryPower} to get the item stack from (can be null)
	 *      @param slot              The (numerical) slot to get the item stack from
	 *      @return                  The item stack from the specified slot
	 */
	public static ItemStack getStack(Entity entity, @Nullable InventoryPower inventoryPower, int slot) {
		return inventoryPower == null ? entity.getStackReference(slot).get() : inventoryPower.getStack(slot);
	}

}
