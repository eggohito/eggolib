package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import io.github.eggohito.eggolib.power.ModifyHurtTicksPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 500)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float eggolib$modifyDamageStuff(float originalValue, DamageSource source, float amount) {

        float newValue = originalValue;
        LivingEntity thisAsLiving = (LivingEntity) (Object) this;

        if (source.getAttacker() != null && source.isProjectile()) newValue = PowerHolderComponent
            .modify(
                source.getAttacker(),
                ModifyProjectileDamagePower.class,
                originalValue,
                mpdp -> mpdp.doesApply(source, originalValue, thisAsLiving),
                mpdp -> mpdp.executeActions(thisAsLiving)
            );

        return newValue;

    }

    @Inject(method = "damage", at = @At(value = "TAIL"))
    private void eggolib$modifyHurtTicks(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        PowerHolderComponent.withPowers(
            (LivingEntity) (Object) this,
            ModifyHurtTicksPower.class,
            mhtp -> mhtp.doesApply(source, amount, source.getAttacker()),
            mhtp -> mhtp.apply(source.getAttacker())
        );
    }

}
