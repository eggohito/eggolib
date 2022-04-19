package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.ResourceOperation;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.mixin.DamageSourceAccessor;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.MathHelper;

public class ChangeHealthAction {

    public static final DamageSource CHANGE_HEALTH_UNDERFLOW = (
        (DamageSourceAccessor) (
            (DamageSourceAccessor) (
                (DamageSourceAccessor) DamageSourceAccessor.createDamageSource("eggolib.change_health.underflow")
            ).callSetBypassesArmor()
        ).callSetUnblockable()
    ).callSetOutOfWorld();

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity instanceof LivingEntity livingEntity)) return;

        ResourceOperation operation = data.get("operation");
        float newHealthValue = data.getFloat("change");
        float oldHealthValue = livingEntity.getHealth();
        float result = oldHealthValue + newHealthValue;

        if (operation == ResourceOperation.ADD) {
            if (result <= 0F) livingEntity.damage(CHANGE_HEALTH_UNDERFLOW, livingEntity.getMaxHealth());
            else livingEntity.setHealth(MathHelper.clamp(result, 1.0F, livingEntity.getMaxHealth()));
        }

        else if (operation == ResourceOperation.SET) {
            if (newHealthValue <= 0F) livingEntity.damage(CHANGE_HEALTH_UNDERFLOW, livingEntity.getMaxHealth());
            else livingEntity.setHealth(MathHelper.clamp(newHealthValue, 1.0F, livingEntity.getMaxHealth()));
        }
    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("change_health"),
            new SerializableData()
                .add("change", SerializableDataTypes.FLOAT)
                .add("operation", ApoliDataTypes.RESOURCE_OPERATION, ResourceOperation.ADD),
            ChangeHealthAction::action
        );
    }
}
