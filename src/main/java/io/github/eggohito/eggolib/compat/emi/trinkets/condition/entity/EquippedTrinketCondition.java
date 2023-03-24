package io.github.eggohito.eggolib.compat.emi.trinkets.condition.entity;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class EquippedTrinketCondition {

    public static boolean condition(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof LivingEntity livingEntity)) return false;

        Predicate<ItemStack> itemCondition = data.get("item_condition");
        Set<String> slots = new HashSet<>();

        data.ifPresent("slot", slots::add);
        data.ifPresent("slots", slots::addAll);

        AtomicInteger matches = new AtomicInteger();
        Optional<TrinketComponent> opt$trinketComponent = TrinketsApi.getTrinketComponent(livingEntity);

        if (opt$trinketComponent.isEmpty()) return false;
        opt$trinketComponent.get().forEach(
            (slotReference, itemStack) -> {

                boolean matching = slots.isEmpty();
                String mergedSlotName = slotReference.inventory().getSlotType().getGroup() + "/" + slotReference.inventory().getSlotType().getName();

                for (String slot : slots.stream().filter(Predicate.not(String::isBlank)).map(StringUtils::deleteWhitespace).toList()) {

                    String[] splitSlot = slot.split("/");

                    if (splitSlot.length == 1 && !splitSlot[0].isBlank()) matching = Pattern.compile("\\b" + splitSlot[0] + "\\b", Pattern.CASE_INSENSITIVE).matcher(mergedSlotName).find();
                    else matching = slot.equalsIgnoreCase(mergedSlotName);

                    if (matching) break;

                }

                if (matching && (itemCondition == null || itemCondition.test(itemStack))) matches.incrementAndGet();

            }
        );

        return matches.get() > 0;

    }

    public static ConditionFactory<Entity> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("equipped_trinket"),
            new SerializableData()
                .add("item_condition", ApoliDataTypes.ITEM_CONDITION, null)
                .add("slot", SerializableDataTypes.STRING, null)
                .add("slots", SerializableDataTypes.STRINGS, null),
            EquippedTrinketCondition::condition
        );
    }

}
