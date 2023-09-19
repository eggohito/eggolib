package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.access.LinkableListenerData;
import io.github.eggohito.eggolib.power.GameEventListenerPower;
import net.minecraft.world.event.Vibrations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Vibrations.ListenerData.class)
public abstract class VibrationListenerDataMixin implements LinkableListenerData {

	@Unique
	private GameEventListenerPower eggolib$linkedPower;

	@Override
	public Optional<GameEventListenerPower> eggolib$getLinkedPower() {
		return Optional.ofNullable(eggolib$linkedPower);
	}

	@Override
	public void eggolib$linkPower(GameEventListenerPower power) {
		this.eggolib$linkedPower = power;
	}

}
