package io.github.eggohito.eggolib.util;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.EggolibModifyDamageDealtPower;
import io.github.eggohito.eggolib.power.EggolibModifyDamageTakenPower;
import io.github.eggohito.eggolib.power.EggolibModifyProjectileDamagePower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.Pair;

public class EggolibMiscUtil {

    public static Pair<Float, Boolean> modifyDamage(Entity target, Entity attacker, DamageSource damageSource, float originalValue) {

        float newValue = originalValue;
        boolean modifiedDamage;

        if (attacker != null && !damageSource.isProjectile()) newValue = PowerHolderComponent
            .modify(
                attacker,
                EggolibModifyDamageDealtPower.class,
                originalValue,
                emddp -> emddp.doesApply(damageSource, originalValue, target),
                emddp -> emddp.executeActions(target)
            );

        if (attacker != null && damageSource.isProjectile()) newValue = PowerHolderComponent
            .modify(
                attacker,
                EggolibModifyProjectileDamagePower.class,
                originalValue,
                empdp -> empdp.doesApply(damageSource, originalValue, target),
                empdp -> empdp.executeActions(target)
            );

        float intermediateValue = newValue;
        newValue = PowerHolderComponent
            .modify(
                target,
                EggolibModifyDamageTakenPower.class,
                intermediateValue,
                emdtp -> emdtp.doesApply(damageSource, intermediateValue, attacker),
                emdtp -> emdtp.executeActions(attacker)
            );

        modifiedDamage = newValue != originalValue;
        return new Pair<>(newValue, modifiedDamage);

    }

}
