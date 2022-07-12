package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyDamageDealtPower;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.apoli.power.ModifyProjectileDamagePower;
import io.github.eggohito.eggolib.Eggolib;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 500)
public abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandOutput {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"))
    private void eggolib$dropAdditionalInventory(CallbackInfo ci) {
        PowerHolderComponent
            .getPowers(this, EggolibInventoryPower.class)
            .forEach(EggolibInventoryPower::dropItemsOnDeath);
    }

    @Inject(method = "damage", at = @At(value = "RETURN", ordinal = 3), cancellable = true)
    private void eggolib$allowDamageIfModifyingPowersExist(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {

        boolean hasModifyingPower = false;

        if (source.getAttacker() != null) {
            if (!source.isProjectile()) hasModifyingPower = PowerHolderComponent
                .hasPower(
                    source.getAttacker(),
                    ModifyDamageDealtPower.class,
                    mddp -> mddp.doesApply(source, amount, this)
                );
            else hasModifyingPower = PowerHolderComponent
                .hasPower(
                    source.getAttacker(),
                    ModifyProjectileDamagePower.class,
                    mpdp -> mpdp.doesApply(source, amount, this)
                );
        }

        hasModifyingPower |= PowerHolderComponent
            .hasPower(
                this,
                ModifyDamageTakenPower.class,
                mdtp -> mdtp.doesApply(source, amount)
            );

        if (hasModifyingPower) cir.setReturnValue(super.damage(source, amount));

    }

}
