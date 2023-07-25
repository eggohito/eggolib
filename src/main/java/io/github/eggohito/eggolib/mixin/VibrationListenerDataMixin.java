package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.power.Power;
import io.github.eggohito.eggolib.access.LinkableListenerData;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Vibrations.ListenerData.class)
public abstract class VibrationListenerDataMixin implements LinkableListenerData {

	@Unique
	private Power eggolib$linkedPower;

	@Override
	public Optional<Power> eggolib$getLinkedPower() {
		return Optional.ofNullable(eggolib$linkedPower);
	}

	@Override
	public void eggolib$linkPower(Power power) {
		this.eggolib$linkedPower = power;
	}

}
