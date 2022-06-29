package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.power.InventoryPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.mixin.ItemSlotArgumentTypeAccessor;
import io.github.eggohito.eggolib.power.EggolibInventoryPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EggolibInventoryUtil {

    public enum InventoryType {
        INVENTORY,
        POWER
    }

    private static Set<Integer> slots;
    private static Consumer<Entity> entityAction;
    private static Predicate<ItemStack> itemCondition;
    private static ActionFactory<Pair<World, ItemStack>>.Instance itemAction;

    public static void modifyInventory(SerializableData.Instance data, Entity entity, Power targetPower) {

        getFields(data);

        if (targetPower == null) {
            slots
                .stream()
                .filter(slot -> entity.getStackReference(slot) != StackReference.EMPTY)
                .filter(slot -> !entity.getStackReference(slot).get().isEmpty())
                .filter(slot -> itemCondition == null || itemCondition.test(entity.getStackReference(slot).get()))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        itemAction.accept(new Pair<>(entity.world, entity.getStackReference(slot).get()));
                    }
                );
        }

        else if (targetPower instanceof InventoryPower inventoryPower) {
            slots.removeIf(slot -> slot < 0 || slot >= inventoryPower.size());
            slots
                .stream()
                .filter(slot -> !inventoryPower.getStack(slot).isEmpty())
                .filter(slot -> itemCondition == null || itemCondition.test(inventoryPower.getStack(slot)))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        itemAction.accept(new Pair<>(entity.world, inventoryPower.getStack(slot)));
                    }
                );
        }

        else if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
            slots.removeIf(slot -> slot < 0 || slot >= eggolibInventoryPower.size());
            slots
                .stream()
                .filter(slot -> !eggolibInventoryPower.getStack(slot).isEmpty())
                .filter(slot -> itemCondition == null || itemCondition.test(eggolibInventoryPower.getStack(slot)))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        itemAction.accept(new Pair<>(entity.world, eggolibInventoryPower.getStack(slot)));
                    }
                );
        }

    }

    public static void replaceInventory(SerializableData.Instance data, Entity entity, Power targetPower) {

        getFields(data);

        ItemStack replacementStack = data.get("stack");

        if (targetPower == null) {
            slots
                .stream()
                .filter(slot -> entity.getStackReference(slot) != StackReference.EMPTY)
                .filter(slot -> itemCondition == null || itemCondition.test(entity.getStackReference(slot).get()))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        entity.getStackReference(slot).set(replacementStack.copy());
                        if (itemAction != null) itemAction.accept(new Pair<>(entity.world, entity.getStackReference(slot).get()));
                    }
                );
        }

        else if (targetPower instanceof InventoryPower inventoryPower) {
            slots.removeIf(slot -> slot < 0 || slot >= inventoryPower.size());
            slots
                .stream()
                .filter(slot -> itemCondition == null || itemCondition.test(inventoryPower.getStack(slot)))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        inventoryPower.setStack(slot, replacementStack.copy());
                        if (itemAction != null) itemAction.accept(new Pair<>(entity.world, inventoryPower.getStack(slot)));
                    }
                );
        }

        else if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
            slots.removeIf(slot -> slot < 0 || slot >= eggolibInventoryPower.size());
            slots
                .stream()
                .filter(slot -> itemCondition == null || itemCondition.test(eggolibInventoryPower.getStack(slot)))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        eggolibInventoryPower.setStack(slot, replacementStack.copy());
                        if (itemAction != null) itemAction.accept(new Pair<>(entity.world, eggolibInventoryPower.getStack(slot)));
                    }
                );
        }

    }

    public static void dropInventory(SerializableData.Instance data, Entity entity, Power targetPower) {

        getFields(data);

        boolean throwRandomly = data.getBoolean("throw_randomly");
        boolean retainOwnership = data.getBoolean("retain_ownership");

        if (targetPower == null) {
            slots
                .stream()
                .filter(slot -> entity.getStackReference(slot) != StackReference.EMPTY)
                .filter(slot -> !entity.getStackReference(slot).get().isEmpty())
                .filter(slot -> itemCondition == null || itemCondition.test(entity.getStackReference(slot).get()))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        if (itemAction != null) itemAction.accept(new Pair<>(entity.world, entity.getStackReference(slot).get()));
                        throwItem(entity, entity.getStackReference(slot).get(), throwRandomly, retainOwnership);
                        entity.getStackReference(slot).set(ItemStack.EMPTY);
                    }
                );
        }

        else if (targetPower instanceof InventoryPower inventoryPower) {
            slots.removeIf(slot -> slot < 0 || slot >= inventoryPower.size());
            slots
                .stream()
                .filter(slot -> !inventoryPower.getStack(slot).isEmpty())
                .filter(slot -> itemCondition == null || itemCondition.test(inventoryPower.getStack(slot)))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        if (itemAction != null) itemAction.accept(new Pair<>(entity.world, inventoryPower.getStack(slot)));
                        throwItem(entity, inventoryPower.getStack(slot), throwRandomly, retainOwnership);
                        inventoryPower.setStack(slot, ItemStack.EMPTY);
                    }
                );
        }

        else if (targetPower instanceof EggolibInventoryPower eggolibInventoryPower) {
            slots.removeIf(slot -> slot < 0 || slot >= eggolibInventoryPower.size());
            slots
                .stream()
                .filter(slot -> !eggolibInventoryPower.getStack(slot).isEmpty())
                .filter(slot -> itemCondition == null || itemCondition.test(eggolibInventoryPower.getStack(slot)))
                .forEach(slot -> {
                        if (entityAction != null) entityAction.accept(entity);
                        if (itemAction != null) itemAction.accept(new Pair<>(entity.world, eggolibInventoryPower.getStack(slot)));
                        throwItem(entity, eggolibInventoryPower.getStack(slot), throwRandomly, retainOwnership);
                        eggolibInventoryPower.setStack(slot, ItemStack.EMPTY);
                    }
                );
        }

    }

    public static void throwItem(Entity thrower, ItemStack itemStack, boolean throwRandomly, boolean retainOwnership) {

        if (itemStack.isEmpty()) return;
        if (thrower instanceof PlayerEntity playerEntity && playerEntity.world.isClient) playerEntity.swingHand(Hand.MAIN_HAND);

        double yOffset = thrower.getEyeY() - 0.30000001192092896;
        ItemEntity itemEntity = new ItemEntity(thrower.world, thrower.getX(), yOffset, thrower.getZ(), itemStack);
        itemEntity.setPickupDelay(40);

        Random random = new Random();

        float f;
        float g;

        if (retainOwnership) itemEntity.setThrower(thrower.getUuid());
        if (throwRandomly) {
            f = random.nextFloat() * 0.5F;
            g = random.nextFloat() * 6.2831855F;
            itemEntity.setVelocity(- MathHelper.sin(g) * f, 0.20000000298023224, MathHelper.cos(g) * f);
        }
        else {
            f = 0.3F;
            g = MathHelper.sin(thrower.getPitch() * 0.017453292F);
            float h = MathHelper.cos(thrower.getPitch() * 0.017453292F);
            float i = MathHelper.sin(thrower.getYaw() * 0.017453292F);
            float j = MathHelper.cos(thrower.getYaw() * 0.017453292F);
            float k = random.nextFloat() * 6.2831855F;
            float l = 0.02F * random.nextFloat();
            itemEntity.setVelocity(
                (double) (-i * h * f) + Math.cos(k) * (double) l,
                (-g * 0.3F + 0.1F + (random.nextFloat() - random.nextFloat()) * 0.1F),
                (double) (j * h * 0.3F) + Math.sin(k) * (double) l
            );

        }

        thrower.world.spawnEntity(itemEntity);

    }

    private static void getFields(SerializableData.Instance data) {

        Set<Integer> itemSlots = new HashSet<>();

        if (data.isPresent("slots")) {
            List<EggolibArgumentWrapper<Integer>> slotNames = data.get("slots");
            itemSlots.addAll(slotNames.stream().map(EggolibArgumentWrapper::get).toList());
        }
        if (data.isPresent("slot")) {
            EggolibArgumentWrapper<Integer> slotName = data.get("slot");
            itemSlots.add(slotName.get());
        }

        if (itemSlots.isEmpty()) {
            Map<String, Integer> slotNamesAndIds = ItemSlotArgumentTypeAccessor.getSlotMappings();
            itemSlots.addAll(slotNamesAndIds.values());
        }

        slots = itemSlots;
        entityAction = data.get("entity_action");
        itemAction = data.get("item_action");
        itemCondition = data.get("item_condition");

    }

}
