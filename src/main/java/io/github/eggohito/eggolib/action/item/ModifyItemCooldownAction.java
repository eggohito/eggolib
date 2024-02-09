package io.github.eggohito.eggolib.action.item;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
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
		Entity stackHolder = stack.getHolder();

		if (stack.isEmpty() || !(stackHolder instanceof PlayerEntity player)) {
			return;
		}

		ItemCooldownManager cooldownManager = player.getItemCooldownManager();
		Item item = stack.getItem();

		ItemCooldownManager.Entry cooldownEntry = cooldownManager.entries.get(item);

		int oldCooldown = cooldownEntry != null ? cooldownEntry.endTick : 0;
		int newCooldown = (int) ModifierUtil.applyModifiers(player, modifiers, oldCooldown);

		cooldownManager.set(item, newCooldown);

	}

	public static ActionFactory<Pair<World, ItemStack>> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("modify_item_cooldown"),
			new SerializableData()
				.add("modifier", Modifier.DATA_TYPE, null)
				.add("modifiers", Modifier.LIST_TYPE, null),
			ModifyItemCooldownAction::action
		);
	}

}
