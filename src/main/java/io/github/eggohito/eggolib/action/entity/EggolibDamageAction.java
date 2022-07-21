package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class EggolibDamageAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        Float damageAmount = data.get("amount");
        Modifier modifier = data.get("modifier");

        if (modifier != null && entity instanceof LivingEntity livingEntity) {

            float maxHealth = livingEntity.getMaxHealth();
            float newDamageAmount = (float) modifier.apply(livingEntity, maxHealth);

            if (newDamageAmount > maxHealth) damageAmount = newDamageAmount - maxHealth;
            else damageAmount = newDamageAmount;

        }

        if (damageAmount != null) entity.damage(data.get("source"), damageAmount);

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("damage"),
            new SerializableData()
                .add("amount", SerializableDataTypes.FLOAT, null)
                .add("source", SerializableDataTypes.DAMAGE_SOURCE)
                .add("modifier", Modifier.DATA_TYPE, null),
            EggolibDamageAction::action
        );
    }

}
