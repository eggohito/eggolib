package io.github.eggohito.eggolib.loot.condition;

import com.google.gson.*;
import io.github.eggohito.eggolib.Eggolib;
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
import java.util.Set;

public class HasTagLootCondition implements LootCondition {

    private final String scoreboardTag;
    private final Set<String> scoreboardTags;

    public HasTagLootCondition() {
        this.scoreboardTag = null;
        this.scoreboardTags = null;
    }

    public HasTagLootCondition(String scoreboardTag) {
        this.scoreboardTag = scoreboardTag;
        this.scoreboardTags = null;
    }

    public HasTagLootCondition(Set<String> scoreboardTags) {
        this.scoreboardTag = null;
        this.scoreboardTags = scoreboardTags;
    }

    public HasTagLootCondition(String scoreboardTag, Set<String> scoreboardTags) {
        this.scoreboardTag = scoreboardTag;
        this.scoreboardTags = scoreboardTags;
    }

    @Override
    public LootConditionType getType() {
        return new LootConditionType(new HasTagLootCondition.Serializer());
    }

    @Override
    public boolean test(LootContext lootContext) {

        Entity entity = lootContext.get(LootContextParameters.THIS_ENTITY);
        if (entity == null) return false;

        Set<String> scoreboardTags = entity.getScoreboardTags();
        Set<String> specifiedScoreboardTags = new HashSet<>();

        if (this.scoreboardTag != null) specifiedScoreboardTags.add(this.scoreboardTag);
        if (this.scoreboardTags != null) specifiedScoreboardTags.addAll(this.scoreboardTags);

        if (specifiedScoreboardTags.isEmpty()) return !scoreboardTags.isEmpty();
        else return !Collections.disjoint(scoreboardTags, specifiedScoreboardTags);

    }

    public static class Serializer implements JsonSerializer<HasTagLootCondition> {

        @Override
        public void toJson(JsonObject jsonObject, HasTagLootCondition hasTagLootCondition, JsonSerializationContext jsonSerializationContext) {

            if (hasTagLootCondition.scoreboardTag != null) jsonObject.addProperty("tag", hasTagLootCondition.scoreboardTag);
            if (hasTagLootCondition.scoreboardTags != null) {

                JsonArray jsonArray = new JsonArray();
                for (String tag : hasTagLootCondition.scoreboardTags) {
                    jsonArray.add(tag);
                }

                jsonObject.add("tags", jsonArray);

            }

        }

        @Override
        public HasTagLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {

            String tag = null;
            Set<String> tags = new HashSet<>();

            if (jsonObject.has("tag")) tag = JsonHelper.getString(jsonObject, "tag");
            if (jsonObject.has("tags")) {
                JsonArray groupsArray = jsonObject.getAsJsonArray("tags");
                for (int i = 0; i < groupsArray.size(); i++) {
                    JsonElement jsonElement = groupsArray.get(i);
                    if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) tags.add(jsonElement.getAsString());
                }
            }

            if (tag != null && !tags.isEmpty()) return new HasTagLootCondition(tag, tags);
            else if (tag == null && !tags.isEmpty()) return new HasTagLootCondition(tags);
            else if (tag != null) return new HasTagLootCondition(tag);
            else return new HasTagLootCondition();

        }

    }

    public static Pair<Identifier, LootConditionType> getIdAndType() {
        return new Pair<>(
            Eggolib.identifier("has_tag"),
            new LootConditionType(new HasTagLootCondition.Serializer())
        );
    }

}
