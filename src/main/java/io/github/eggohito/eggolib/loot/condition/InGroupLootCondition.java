package io.github.eggohito.eggolib.loot.condition;

import com.google.gson.*;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.EggolibEntityComponents;
import io.github.eggohito.eggolib.component.entity.GroupComponent;
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

public class InGroupLootCondition implements LootCondition {

    private final String group;
    private final Set<String> groups;

    public InGroupLootCondition() {
        this.group = null;
        this.groups = null;
    }

    public InGroupLootCondition(String group) {
        this.group = group;
        this.groups = null;
    }

    public InGroupLootCondition(Set<String> groups) {
        this.group = null;
        this.groups = groups;
    }

    public InGroupLootCondition(String group, Set<String> groups) {
        this.group = group;
        this.groups = groups;
    }

    @Override
    public LootConditionType getType() {
        return new LootConditionType(new InGroupLootCondition.Serializer());
    }

    @Override
    public boolean test(LootContext lootContext) {

        Optional<GroupComponent> groupComponent = EggolibEntityComponents.GROUP.maybeGet(lootContext.get(LootContextParameters.THIS_ENTITY));
        if (groupComponent.isEmpty()) return false;

        Set<String> groupsFromComponent = groupComponent.get().getGroups();
        Set<String> specifiedGroups = new HashSet<>();

        if (group != null) specifiedGroups.add(group);
        if (groups != null) specifiedGroups.addAll(groups);

        if (specifiedGroups.isEmpty()) return !groupsFromComponent.isEmpty();
        else return !Collections.disjoint(groupsFromComponent, specifiedGroups);

    }

    public static class Serializer implements JsonSerializer<InGroupLootCondition> {

        @Override
        public void toJson(JsonObject jsonObject, InGroupLootCondition inGroupLootCondition, JsonSerializationContext jsonSerializationContext) {

            if (inGroupLootCondition.group != null) jsonObject.addProperty("group", inGroupLootCondition.group);
            if (inGroupLootCondition.groups != null) {

                JsonArray jsonArray = new JsonArray();
                for (String group : inGroupLootCondition.groups) {
                    jsonArray.add(group);
                }

                jsonObject.add("groups", jsonArray);

            }

        }

        @Override
        public InGroupLootCondition fromJson(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {

            String group = null;
            Set<String> groups = new HashSet<>();

            if (jsonObject.has("group")) group = JsonHelper.getString(jsonObject, "group");
            if (jsonObject.has("groups")) {
                JsonArray groupsArray = jsonObject.getAsJsonArray("groups");
                for (int i = 0; i < groupsArray.size(); i++) {
                    JsonElement jsonElement = groupsArray.get(i);
                    if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()) groups.add(jsonElement.getAsString());
                }
            }

            if (group != null && !groups.isEmpty()) return new InGroupLootCondition(group, groups);
            else if (group == null && !groups.isEmpty()) return new InGroupLootCondition(groups);
            else if (group != null) return new InGroupLootCondition(group);
            else return new InGroupLootCondition();

        }

    }

    public static Pair<Identifier, LootConditionType> getIdAndType() {
        return new Pair<>(
            Eggolib.identifier("in_group"),
            new LootConditionType(new InGroupLootCondition.Serializer())
        );
    }

}
