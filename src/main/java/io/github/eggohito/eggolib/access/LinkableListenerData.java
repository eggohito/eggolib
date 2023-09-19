package io.github.eggohito.eggolib.access;

import io.github.eggohito.eggolib.power.GameEventListenerPower;

import java.util.Optional;

public interface LinkableListenerData {
	Optional<GameEventListenerPower> eggolib$getLinkedPower();
	void eggolib$linkPower(GameEventListenerPower power);
}
