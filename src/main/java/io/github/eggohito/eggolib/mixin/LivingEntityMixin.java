package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.ModifyHurtTicksPower;
import io.github.eggohito.eggolib.util.EggolibMiscUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LivingEntity.class, priority = 500)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private boolean eggolib$modifiedDamage;

    @ModifyVariable(method = "damage", at = @At("HEAD"), argsOnly = true)
    private float eggolib$modifyDamageStuff(float originalValue, DamageSource source, float amount) {

        LivingEntity thisAsLiving = (LivingEntity) (Object) this;
        Pair<Float, Boolean> modifiedDamage = new Pair<>(originalValue, false);

        if (!(thisAsLiving instanceof PlayerEntity)) modifiedDamage = EggolibMiscUtil
            .modifyDamage(
                thisAsLiving,
                source.getAttacker(),
                source,
                originalValue
            );

        eggolib$modifiedDamage = modifiedDamage.getRight();
        return modifiedDamage.getLeft();

    }

    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSleeping()Z"), cancellable = true)
    private void eggolib$preventHitIfDamageIsZero(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (eggolib$modifiedDamage && amount <= 0F) cir.setReturnValue(false);
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
