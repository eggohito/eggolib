package io.github.eggohito.eggolib.access;

import io.github.apace100.apoli.power.Power;

import java.util.Optional;

public interface LinkableListenerData {
	Optional<Power> eggolib$getLinkedPower();
	void eggolib$linkPower(Power power);
}
