package io.github.eggohito.eggolib.action.entity;

import com.google.common.collect.Sets;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.AdvancementCommandAccessor;
import net.minecraft.advancement.Advancement;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.command.AdvancementCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class GrantAdvancementAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		MinecraftServer server = entity.getServer();
		if (server == null || !(entity instanceof ServerPlayerEntity serverPlayerEntity)) {
			return;
		}

		ServerAdvancementLoader advancementLoader = server.getAdvancementLoader();
		AdvancementCommand.Selection selection = data.get("selection");

		if (selection == AdvancementCommand.Selection.EVERYTHING) {
			grantAdvancements(serverPlayerEntity, new LinkedList<>(advancementLoader.getAdvancements()));
		} else if (data.isPresent("advancement")) {

			Identifier advancementId = data.get("advancement");
			Advancement advancement = advancementLoader.get(advancementId);
			if (advancement == null) {
				Eggolib.LOGGER.warn("Unknown advancement (\"" + advancementId + "\") referenced in `grant_advancement` entity action type!");
				return;
			}

			Set<String> criteria = Sets.newHashSet();

			data.ifPresent("criterion", criteria::add);
			data.ifPresent("criteria", criteria::addAll);

			if (criteria.isEmpty()) {
				grantAdvancements(serverPlayerEntity, AdvancementCommandAccessor.callSelect(advancement, selection));
			} else {
				grantCriteria(serverPlayerEntity, advancement, criteria);
			}

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
