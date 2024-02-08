package io.github.eggohito.eggolib.action.item;

import io.github.apace100.apoli.access.EntityLinkedItemStack;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.item.ItemActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

public class ModifyItemCooldownAction {

	public static void action(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {

		List<Modifier> modifiers = new LinkedList<>();

		data.<Modifier>ifPresent("modifier", modifiers::add);
		data.<List<Modifier>>ifPresent("modifiers", modifiers::addAll);

		ItemStack stack = worldAndStack.getRight();
		Entity stackHolder = ((EntityLinkedItemStack) stack).apoli$getEntity();

		if (stack.isEmpty() || !(stackHolder instanceof PlayerEntity player)) {
			return;
		}

		ItemCooldownManager cooldownManager = player.getItemCooldownManager();
		Item item = stack.getItem();

		double newCooldown = ModifierUtil.applyModifiers(player, modifiers, cooldownManager.getCooldownProgress(item, 0.0f));
		cooldownManager.set(item, (int) Math.floor(newCooldown));

	}

	public static ActionFactory<Pair<World, StackReference>> getFactory() {
		return ItemActionFactory.createItemStackBased(
			Eggolib.identifier("modify_item_cooldown"),
			new SerializableData()
				.add("modifier", Modifier.DATA_TYPE, null)
				.add("modifiers", Modifier.LIST_TYPE, null),
			ModifyItemCooldownAction::action
		);
	}

}
