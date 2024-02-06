package io.github.eggohito.eggolib.loot.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.*;

public record HasTagLootCondition(Optional<String> commandTag, Optional<List<String>> commandTags) implements LootCondition {

	public static final Codec<HasTagLootCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.optionalFieldOf("tag").forGetter(HasTagLootCondition::commandTag),
		Codec.STRING.listOf().optionalFieldOf("tags").forGetter(HasTagLootCondition::commandTags)
	).apply(instance, HasTagLootCondition::new));
	public static final LootConditionType TYPE = new LootConditionType(CODEC);

	@Override
	public LootConditionType getType() {
		return TYPE;
	}

	@Override
	public boolean test(LootContext lootContext) {

		Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
		if (entity == null) {
			return false;
		}

		Set<String> commandTags = entity.getCommandTags();
		Set<String> specifiedCommandTags = new HashSet<>();

		this.commandTag.ifPresent(specifiedCommandTags::add);
		this.commandTags.ifPresent(specifiedCommandTags::addAll);

		return specifiedCommandTags.isEmpty()
			? !commandTags.isEmpty()
			: !Collections.disjoint(commandTags, specifiedCommandTags);

	}

	public static Pair<Identifier, LootConditionType> getIdAndType() {
		return new Pair<>(
			Eggolib.identifier("has_tag"),
			TYPE
		);
	}

}
