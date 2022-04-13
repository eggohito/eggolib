package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyDamageDealtPower;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import io.github.eggohito.eggolib.power.EggolibInventoryPower;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandOutput {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void eggolib$dropAdditionalInventory(CallbackInfo ci) {
        PowerHolderComponent.getPowers(this, EggolibInventoryPower.class).forEach(
            eggolibInventoryPower -> {
                if (eggolibInventoryPower.shouldDropOnDeath()) eggolibInventoryPower.dropItemsOnDeath();
            }
        );
    }

    private boolean eggolib$modifiedDamage;

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float eggolib$modifyDamageStuff(float originalValue, DamageSource source, float amount) {

        float newValue = originalValue;

        if (source.getAttacker() != null && !source.isProjectile()) {
            newValue = PowerHolderComponent.modify(
                source.getAttacker(),
                ModifyDamageDealtPower.class,
                originalValue,
                mddp -> mddp.doesApply(source, originalValue, this),
                mddp -> mddp.executeActions(this)
            );
        }

        if (source.getAttacker() != null && source.isProjectile()) {
            newValue = PowerHolderComponent.modify(
                source.getAttacker(),
                ModifyProjectileDamagePower.class,
                originalValue,
                mpdp -> mpdp.doesApply(source, originalValue, this),
                mpdp -> mpdp.executeActions(this)
            );
        }

        float intermediateValue = newValue;

        newValue = PowerHolderComponent.modify(
            this,
            ModifyDamageTakenPower.class,
            intermediateValue,
            mdtp -> mdtp.doesApply(source, intermediateValue),
            mdtp -> mdtp.executeActions(source.getAttacker())
        );

        eggolib$modifiedDamage = newValue != originalValue;

        return newValue;
    }

    @Inject(method = "damage", at = @At(value = "TAIL"), cancellable = true)
    private void eggolib$preventHitIfDamageIsZero(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (eggolib$modifiedDamage && amount == 0.0F) {
            cir.setReturnValue(false);
        }
    }
}
