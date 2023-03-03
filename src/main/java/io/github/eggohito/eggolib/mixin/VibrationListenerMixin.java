package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.access.VibrationListenerAccess;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.PositionSource;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(VibrationListener.class)
public abstract class VibrationListenerMixin implements GameEventListener, VibrationListenerAccess {

    @Shadow public PositionSource positionSource;

    @Shadow public abstract void tick(World world);

    @Unique private boolean eggolib$showParticle = true;

    @WrapOperation(method = "method_45495", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
    private <T extends ParticleEffect> int eggolib$handleParticleEffectVisibility(ServerWorld instance, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, Operation<Integer> original) {
        return shouldShowParticle() ? original.call(instance, particle, x, y, z, count, deltaX, deltaY, deltaZ, speed) : 0;
    }

    @Override
    public boolean shouldShowParticle() {
        return eggolib$showParticle;
    }

    @Override
    public void showParticle(boolean bl) {
        this.eggolib$showParticle = bl;
    }

    @Override
    public void tickWithPositionSource(World world, PositionSource positionSource) {
        this.positionSource = positionSource;
        this.tick(world);
    }

}
