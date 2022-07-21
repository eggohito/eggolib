package io.github.eggohito.eggolib.action.bientity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.Pair;

public class EggolibDamageAction {

    public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

        Float damageAmount = data.get("amount");
        Modifier modifier = data.get("modifier");

        if (modifier != null && actorAndTarget.getRight() instanceof LivingEntity livingTargetEntity) {

            float targetMaxHealth = livingTargetEntity.getMaxHealth();
            float newDamageAmount = (float) modifier.apply(livingTargetEntity, targetMaxHealth);

            if (newDamageAmount > targetMaxHealth) damageAmount = newDamageAmount - targetMaxHealth;
            else damageAmount = newDamageAmount;

        }

        DamageSource damageSource = data.get("source");
        EntityDamageSource entityDamageSource = new EntityDamageSource(damageSource.getName(), actorAndTarget.getLeft());

        if (damageSource.isExplosive()) entityDamageSource.setExplosive();
        if (damageSource.isProjectile()) entityDamageSource.setProjectile();
        if (damageSource.isFromFalling()) entityDamageSource.setFromFalling();
        if (damageSource.isMagic()) entityDamageSource.setUsesMagic();
        if (damageSource.isNeutral()) entityDamageSource.setNeutral();

        if (damageAmount != null) actorAndTarget.getRight().damage(entityDamageSource, damageAmount);

    }

    public static ActionFactory<Pair<Entity, Entity>> getFactory() {
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
