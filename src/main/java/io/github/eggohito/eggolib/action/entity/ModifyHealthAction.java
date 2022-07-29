package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.content.EggolibDamageSources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ModifyHealthAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof LivingEntity livingEntity)) return;

        Modifier modifier = data.get("modifier");
        float result = (float) modifier.apply(livingEntity, livingEntity.getMaxHealth());

        if (result <= 0F) livingEntity.damage(EggolibDamageSources.CHANGE_HEALTH_UNDERFLOW, livingEntity.getMaxHealth());
        else livingEntity.setHealth(result);

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("modify_health"),
            new SerializableData()
                .add("modifier", Modifier.DATA_TYPE),
            ModifyHealthAction::action
        );
    }

}
