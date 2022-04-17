package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class InventoryUtil {

    public enum InventoryType {
        ENDER_CHEST,
        PLAYER,
        POWER
    }

    public static void drop(SerializableData.Instance data, PlayerEntity playerEntity, Inventory inventory) {

        Set<Integer> slots = null;
        if (data.isPresent("slots")) slots = new HashSet<>(data.get("slots"));

        Consumer<Entity> entityAction = data.get("entity_action");
        Predicate<ItemStack> itemCondition = data.get("item_condition");
        boolean throwRandomly = data.getBoolean("throw_randomly");
        boolean retainOwnership = data.getBoolean("retain_ownership");

        for (int i = 0; i < inventory.size(); i++) {
            if (slots != null && !slots.contains(i)) continue;
            ItemStack currentItemStack = inventory.getStack(i);
            if (!currentItemStack.isEmpty()) {
                if (itemCondition == null || itemCondition.test(currentItemStack)) {
                    if (entityAction != null) entityAction.accept(playerEntity);
                    playerEntity.dropItem(currentItemStack, throwRandomly, retainOwnership);
                    inventory.setStack(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public static void modify(SerializableData.Instance data, PlayerEntity playerEntity, Inventory inventory) {

        Set<Integer> slots = null;
        if (data.isPresent("slots")) slots = new HashSet<>(data.get("slots"));

        Consumer<Entity> entityAction = data.get("entity_action");
        Predicate<ItemStack> itemCondition = data.get("item_condition");
        ActionFactory<Pair<World, ItemStack>>.Instance itemAction = data.get("item_action");

        for (int i = 0; i < inventory.size(); i++) {
            if (slots != null && !slots.contains(i)) continue;
            ItemStack currentItemStack = inventory.getStack(i);
            if (!currentItemStack.isEmpty()) {
                if (itemCondition == null || itemCondition.test(currentItemStack)) {
                    if (entityAction != null) entityAction.accept(playerEntity);
                    itemAction.accept(new Pair<>(playerEntity.world, currentItemStack));
                }
            }
        }
    }

    public static void replace(SerializableData.Instance data, PlayerEntity playerEntity, Inventory inventory) {

        Set<Integer> slots = null;
        if (data.isPresent("slots")) slots = new HashSet<>(data.get("slots"));

        ItemStack replacementStack = data.get("stack");
        Consumer<Entity> entityAction = data.get("entity_action");
        Predicate<ItemStack> itemCondition = data.get("item_condition");
        ActionFactory<Pair<World, ItemStack>>.Instance itemAction = data.get("item_action");

        for (int i = 0; i < inventory.size(); i++) {
            if (slots != null && !slots.contains(i)) continue;
            ItemStack currentItemStack = inventory.getStack(i);
            if (itemCondition == null || itemCondition.test(currentItemStack)) {
                if (entityAction != null) entityAction.accept(playerEntity);
                inventory.setStack(i, replacementStack.copy());
                if (itemAction != null) itemAction.accept(new Pair<>(playerEntity.world, inventory.getStack(i)));
            }
        }
    }
}
