package io.github.eggohito.eggolib.loot.condition;

import com.google.gson.*;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibComponents;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.Pair;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class HasTagLootCondition implements LootCondition {

	private final Optional<String> commandTag;
	private final Optional<Set<String>> commandTags;

	public HasTagLootCondition(Optional<String> commandTag, Optional<Set<String>> commandTags) {
		this.commandTag = commandTag;
		this.commandTags = commandTags;
	}

	@Override
	public LootConditionType getType() {
		return new LootConditionType(new HasTagLootCondition.Serializer());
	}

	@Override
	public boolean test(LootContext lootContext) {

		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity == null) {
			return false;
		}

		Set<String> scoreboardTags = EggolibComponents.MISC.get(entity).getCommandTags();
		Set<String> specifiedScoreboardTags = new HashSet<>();


		this.commandTag.ifPresent(specifiedScoreboardTags::add);
		this.commandTags.ifPresent(specifiedScoreboardTags::addAll);

		return specifiedScoreboardTags.isEmpty()
			? !scoreboardTags.isEmpty()
			: !Collections.disjoint(scoreboardTags, specifiedScoreboardTags);

	}

	public static class Serializer implements JsonSerializer<HasTagLootCondition> {

		@Override
		public void toJson(JsonObject jsonObject, HasTagLootCondition hasTagLootCondition, JsonSerializationContext jsonSerializationContext) {

			hasTagLootCondition.commandTag.ifPresent(commandTag ->
				jsonObject.addProperty("tag", commandTag)
			);

			hasTagLootCondition.commandTags.ifPresent(commandTags -> {

				JsonArray jsonArray = new JsonArray();
				commandTags.forEach(jsonArray::add);

				jsonObject.add("tags", jsonArray);

			});

		}

		@Override
		public HasTagLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {

			String commandTag = null;
			Set<String> commandTags = null;

			if (jsonObject.has("tag")) {
				commandTag = JsonHelper.getString(jsonObject, "tag");
			}

			if (jsonObject.has("tags")) {

				JsonArray jsonArray = JsonHelper.getArray(jsonObject, "tags");
				commandTags = new HashSet<>();

				for (int i = 0; i < jsonArray.size(); i++) {

					JsonElement jsonElement = jsonArray.get(i);

					if (jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString()) {
						commandTags.add(jsonPrimitive.getAsString());
					}

				}

			}

			//noinspection OptionalOfNullableMisuse
			return new HasTagLootCondition(
				Optional.ofNullable(commandTag),
				Optional.ofNullable(commandTags)
			);

		}

	}

	public static Pair<Identifier, LootConditionType> getIdAndType() {
		return new Pair<>(
			Eggolib.identifier("has_tag"),
			new LootConditionType(new HasTagLootCondition.Serializer())
		);
	}

}
