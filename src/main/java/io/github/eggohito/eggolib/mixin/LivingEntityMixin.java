package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.access.ModifiableFoodEntity;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyDamageDealtPower;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 2000)
public abstract class LivingEntityMixin extends Entity implements ModifiableFoodEntity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private boolean eggolib$modifiedDamage;

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float eggolib$modifyDamageStuff(float originalValue, DamageSource source, float amount) {

        float newValue = originalValue;
        LivingEntity thisAsLiving = (LivingEntity) (Object) this;

        if (!(thisAsLiving instanceof PlayerEntity)) {

            if (source.getAttacker() != null && !source.isProjectile()) {
                newValue = PowerHolderComponent.modify(
                    source.getAttacker(),
                    ModifyDamageDealtPower.class,
                    originalValue,
                    mddp -> mddp.doesApply(source, originalValue, thisAsLiving),
                    mddp -> mddp.executeActions(thisAsLiving)
                );
            }

            if (source.getAttacker() != null && source.isProjectile()) {
                newValue = PowerHolderComponent.modify(
                    source.getAttacker(),
                    ModifyProjectileDamagePower.class,
                    originalValue,
                    mpdp -> mpdp.doesApply(source, originalValue, thisAsLiving),
                    mpdp -> mpdp.executeActions(thisAsLiving)
                );
            }

            float intermediateValue = newValue;

            newValue = PowerHolderComponent.modify(
                thisAsLiving,
                ModifyDamageTakenPower.class,
                intermediateValue,
                mdtp -> mdtp.doesApply(source, intermediateValue),
                mdtp -> mdtp.executeActions(source.getAttacker())
            );
        }

        eggolib$modifiedDamage = newValue != originalValue;

        return newValue;
    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"), cancellable = true)
    private void eggolib$preventHitIfDamageIsZero(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (eggolib$modifiedDamage && amount == 0.0F) {
            cir.setReturnValue(false);
        }
    }
}
