package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.AdvancementCommandAccessor;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AdvancementCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrantAdvancementAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		MinecraftServer server = entity.getServer();
		if (server == null || !(entity instanceof ServerPlayerEntity serverPlayerEntity)) {
			return;
		}

		Identifier advancementId = data.get("advancement");
		Advancement advancement = server.getAdvancementLoader().get(advancementId);
		if (advancement == null) {
			return;
		}

		AdvancementCommand.Selection selection = data.get("selection");
		List<Advancement> selectedAdvancements = AdvancementCommandAccessor.callSelect(advancement, selection);

		if (!(data.isPresent("criteria") || data.isPresent("criterion")) || selection == AdvancementCommand.Selection.EVERYTHING) {
			grantAdvancements(serverPlayerEntity, selectedAdvancements);
		} else {

			Set<String> criteria = new HashSet<>();

			data.ifPresent("criterion", criteria::add);
			data.ifPresent("criteria", criteria::addAll);

			grantCriteria(serverPlayerEntity, advancement, criteria);

		}

	}

	private static void grantCriteria(ServerPlayerEntity serverPlayerEntity, Advancement advancement, Set<String> criteria) {
		for (String criterion : criteria.stream().filter(c -> advancement.getCriteria().containsKey(c)).toList()) {
			AdvancementCommand.Operation.GRANT.processEachCriterion(serverPlayerEntity, advancement, criterion);
		}
	}

	private static void grantAdvancements(ServerPlayerEntity serverPlayerEntity, List<Advancement> advancements) {
		for (Advancement advancement : advancements) {
			AdvancementCommand.Operation.GRANT.processEach(serverPlayerEntity, advancement);
		}
	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("grant_advancement"),
			new SerializableData()
				.add("advancement", SerializableDataTypes.IDENTIFIER, null)
				.add("criterion", SerializableDataTypes.STRING, null)
				.add("criteria", SerializableDataTypes.STRINGS, null)
				.add("selection", EggolibDataTypes.ADVANCEMENT_SELECTION, AdvancementCommand.Selection.ONLY),
			GrantAdvancementAction::action
		);
	}

}
