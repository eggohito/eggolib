package io.github.eggohito.eggolib.mixin.apace100.apoli;

import io.github.apace100.apoli.component.PowerHolderComponentImpl;
import io.github.apace100.apoli.power.PowerType;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PowerHolderComponentImpl.class)
public abstract class PowerHolderComponentImplMixin {

    @Shadow @Final private LivingEntity owner;

    @Inject(method = "removePower", at = @At(value = "INVOKE", target = "Lio/github/apace100/apoli/power/Power;onLost()V"), remap = false)
    private void eggolib$removeCurrentScreenStatusOnPowerRemoval(PowerType<?> powerType, Identifier source, CallbackInfo ci) {
        eggolib$removeCurrentScreenStatus(owner);
    }

    @Inject(method = "fromTag", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/ConcurrentHashMap;clear()V"))
    private void eggolib$removeCurrentScreenStatusOnTagRead(NbtCompound compoundTag, boolean callPowerOnAdd, CallbackInfo ci) {
        eggolib$removeCurrentScreenStatus(owner);
    }

    private void eggolib$removeCurrentScreenStatus(Entity entity) {
        if (!(entity instanceof PlayerEntity playerEntity)) return;
        Eggolib.PLAYERS_IN_SCREEN.remove(playerEntity);
    }

}
