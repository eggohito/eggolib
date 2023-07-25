package io.github.eggohito.eggolib.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.eggohito.eggolib.util.VibrationsTickerUtil;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vibrations.Ticker.class)
public interface VibrationsTickerMixin {

	@Inject(method = "tick", at = @At("HEAD"))
	private static void eggolib$cacheListenerData(World world, Vibrations.ListenerData listenerData, Vibrations.Callback callback, CallbackInfo ci) {
		VibrationsTickerUtil.cache(listenerData);
	}

	@WrapOperation(method = {"method_51408", "spawnVibrationParticle"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"))
	private static <T extends ParticleEffect> int eggolib$preventVibrationParticle(ServerWorld instance, T particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed, Operation<Integer> original) {
		return VibrationsTickerUtil.preventParticle() ? 0 : original.call(instance, particle, x, y, z, count, deltaX, deltaY, deltaZ, speed);
	}

}
