package io.github.eggohito.eggolib.action.entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectorAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		MinecraftServer server = entity.getServer();
		if (server == null) {
			return;
		}

		ArgumentWrapper<EntitySelector> selectorWrapper = data.get("selector");
		EntitySelector selector = selectorWrapper.get();
		Consumer<Pair<Entity, Entity>> biEntityAction = data.get("bientity_action");
		Predicate<Pair<Entity, Entity>> biEntityCondition = data.get("bientity_condition");

		try {
			for (Entity target : selector.getEntities(entity.getCommandSource())) {

				Pair<Entity, Entity> actorAndTarget = new Pair<>(entity, target);

				if (biEntityCondition == null || biEntityCondition.test(actorAndTarget)) {
					biEntityAction.accept(actorAndTarget);
				}

			}
		} catch (CommandSyntaxException ignored) {

		}

	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("selector_action"),
			new SerializableData()
				.add("selector", EggolibDataTypes.ENTITIES_SELECTOR)
				.add("bientity_action", ApoliDataTypes.BIENTITY_ACTION)
				.add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null),
			SelectorAction::action
		);
	}

}
