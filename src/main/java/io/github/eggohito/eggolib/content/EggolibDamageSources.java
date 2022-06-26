package io.github.eggohito.eggolib.content;

import io.github.apace100.calio.mixin.DamageSourceAccessor;
import net.minecraft.entity.damage.DamageSource;

public class EggolibDamageSources {

    public static final DamageSource CHANGE_HEALTH_UNDERFLOW = (
        (DamageSourceAccessor) (
            (DamageSourceAccessor) (
                (DamageSourceAccessor) DamageSourceAccessor.createDamageSource("eggolib.change_health.underflow")
            ).callSetBypassesArmor()
        ).callSetUnblockable()
    ).callSetOutOfWorld();

}
