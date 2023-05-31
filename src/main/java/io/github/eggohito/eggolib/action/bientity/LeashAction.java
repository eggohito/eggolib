package io.github.eggohito.eggolib.action.bientity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Pair;

public class LeashAction {

	public static void action(SerializableData.Instance data, Pair<Entity, Entity> actorAndTarget) {

		Entity actor = actorAndTarget.getLeft();
		Entity target = actorAndTarget.getRight();

		if (target instanceof MobEntity targetMob) {
			targetMob.attachLeash(actor, true);
		}

	}

	public static ActionFactory<Pair<Entity, Entity>> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("leash"),
			new SerializableData(),
			LeashAction::action
		);
	}

}
