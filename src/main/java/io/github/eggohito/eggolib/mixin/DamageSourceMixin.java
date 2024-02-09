package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.access.DamageVictim;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin implements DamageVictim {

	@Unique
	Entity eggolib$target;

	@Override
	public Entity eggolib$get() {
		return eggolib$target;
	}

	@Override
	public void eggolib$set(Entity entity) {
		this.eggolib$target = entity;
	}

}
