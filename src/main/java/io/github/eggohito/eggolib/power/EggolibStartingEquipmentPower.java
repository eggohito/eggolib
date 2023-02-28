package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.ItemSlotArgumentTypeAccessor;
import io.github.eggohito.eggolib.util.InventoryUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;

import java.util.*;

public class EggolibStartingEquipmentPower extends Power {

    private final boolean recurrent;
    private final List<ItemStack> itemStacks = new ArrayList<>();
    private final HashMap<Integer, ItemStack> positionedItemStacks = new HashMap<>();

    public EggolibStartingEquipmentPower(PowerType<?> powerType, LivingEntity livingEntity, boolean recurrent) {
        super(powerType, livingEntity);
        this.recurrent = recurrent;
    }

    @Override
    public void onGained() {
        giveStacks();
    }

    @Override
    public void onRespawn() {
        if (recurrent) giveStacks();
    }

    public void addStack(Pair<ArgumentWrapper<Integer>, ItemStack> slotAndStackPair) {
        if (slotAndStackPair.getLeft() == null) itemStacks.add(slotAndStackPair.getRight());
        else positionedItemStacks.put(slotAndStackPair.getLeft().get(), slotAndStackPair.getRight());
    }

    public void addStacks(List<Pair<ArgumentWrapper<Integer>, ItemStack>> slotAndStackPairs) {
        slotAndStackPairs.forEach(this::addStack);
    }

    private void giveOrDropStacks(HashMap<Integer, ItemStack> slotAndStackMap) {
        for (Integer slot : slotAndStackMap.keySet()) {

            StackReference stackReference = entity.getStackReference(slot);
            ItemStack stack = slotAndStackMap.get(slot);
            boolean set = false;

            if (stackReference.get().isEmpty()) set = stackReference.set(stack);
            if (!set) InventoryUtil.throwItem(entity, stack, false, true);

        }
    }

    private void giveOrDropStacks(List<ItemStack> stacks) {
        List<Integer> slots = ItemSlotArgumentTypeAccessor.getSlotMappings().values().stream().sorted().toList();
        List<StackReference> stackReferences = slots.stream().map(entity::getStackReference).filter(stackReference -> stackReference != StackReference.EMPTY).toList();
        for (ItemStack stack : stacks) {

            boolean set = false;

            for (StackReference stackReference : stackReferences) {
                if (stackReference.get().isEmpty() && stackReference.set(stack)) {
                    set = true;
                    break;
                }
            }

            if (!set) InventoryUtil.throwItem(entity, stack, false, true);

        }
    }

    private void giveStacks() {
        giveOrDropStacks(positionedItemStacks);
		giveOrDropStacks(itemStacks);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("starting_equipment"),
            new SerializableData()
                .add("stack", EggolibDataTypes.GENERALIZED_POSITIONED_ITEM_STACK, null)
                .add("stacks", EggolibDataTypes.GENERALIZED_POSITIONED_ITEM_STACKS, null)
                .add("recurrent", SerializableDataTypes.BOOLEAN, false),
            data -> (powerType, livingEntity) -> {

                EggolibStartingEquipmentPower esep = new EggolibStartingEquipmentPower(
                    powerType,
                    livingEntity,
                    data.getBoolean("recurrent")
                );

                data.ifPresent("stack", esep::addStack);
                data.ifPresent("stacks", esep::addStacks);

                return esep;

            }
        ).allowCondition();
    }

}
