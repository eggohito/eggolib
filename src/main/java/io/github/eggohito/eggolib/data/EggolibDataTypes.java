package io.github.eggohito.eggolib.data;

import com.google.gson.JsonPrimitive;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.registry.EggolibConditionTypes;
import io.github.eggohito.eggolib.util.*;
import io.github.eggohito.eggolib.util.EggolibMathUtil.MathOperation;
import io.github.eggohito.eggolib.util.key.FunctionalKey;
import io.github.eggohito.eggolib.util.key.TimedKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.util.Pair;
import net.minecraft.world.dimension.DimensionType;

import java.util.EnumSet;
import java.util.List;

public class EggolibDataTypes {

    public static final SerializableDataType<ConditionFactory<DimensionType>.Instance> DIMENSION_CONDITION = new SerializableDataType<>(
        ClassUtil.castClass(ConditionFactory.Instance.class),
        EggolibConditionTypes.DIMENSION::write,
        EggolibConditionTypes.DIMENSION::read,
        EggolibConditionTypes.DIMENSION::read
    );

    public static final SerializableDataType<List<ConditionFactory<DimensionType>.Instance>> DIMENSION_CONDITIONS = SerializableDataType.list(DIMENSION_CONDITION);

    public static final SerializableDataType<MathOperation> MATH_OPERATION = SerializableDataType.enumValue(EggolibMathUtil.MathOperation.class);

    public static final SerializableDataType<EggolibToolType> TOOL_TYPE = SerializableDataType.enumValue(EggolibToolType.class);

    public static final SerializableDataType<EnumSet<EggolibToolType>> TOOL_TYPE_SET = SerializableDataType.enumSet(EggolibToolType.class, TOOL_TYPE);

    public static final SerializableDataType<EggolibPerspective> PERSPECTIVE = SerializableDataType.enumValue(EggolibPerspective.class);

    public static final SerializableDataType<EnumSet<EggolibPerspective>> PERSPECTIVE_SET = SerializableDataType.enumSet(EggolibPerspective.class, PERSPECTIVE);

    public static final SerializableDataType<Pair<String, String>> SCOREBOARD = SerializableDataType.compound(
        ClassUtil.castClass(Pair.class),
        new SerializableData()
            .add("name", SerializableDataTypes.STRING)
            .add("objective", SerializableDataTypes.STRING),
        data -> new Pair<>(data.getString("name"), data.getString("objective")),
        (serializableData, nameAndObjective) -> {

            SerializableData.Instance instance = serializableData.new Instance();
            instance.set("name", nameAndObjective.getLeft());
            instance.set("objective", nameAndObjective.getRight());

            return instance;

        }
    );

    public static final SerializableDataType<Key> KEY = SerializableDataType.compound(
        Key.class,
        new SerializableData()
            .add("key", SerializableDataTypes.STRING),
        data -> new Key(
            data.getString("key")
        ),
        (serializableData, key) -> {

            SerializableData.Instance data = serializableData.new Instance();
            data.set("key", key.key);

            return data;

        }
    );

    public static final SerializableDataType<Key> BACKWARDS_COMPATIBLE_KEY = new SerializableDataType<>(
        Key.class,
        KEY::send,
        KEY::receive,
        jsonElement -> {
            if (!(jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString())) return KEY.read(jsonElement);
            else return new Key(jsonPrimitive.getAsString());
        }
    );

    public static final SerializableDataType<TimedKey> TIMED_KEY = SerializableDataType.compound(
        TimedKey.class,
        new SerializableData()
            .add("key", SerializableDataTypes.STRING)
            .add("ticks", SerializableDataTypes.INT, 0)
            .add("offset", SerializableDataTypes.INT, 20),
        data -> new TimedKey(
            data.getString("key"),
            data.getInt("ticks"),
            data.getInt("offset")
        ),
        (serializableData, timedKey) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("key", timedKey.key);
            data.set("ticks", timedKey.ticks);
            data.set("offset", timedKey.offset);

            return data;

        }
    );

    public static final SerializableDataType<TimedKey> BACKWARDS_COMPATIBLE_TIMED_KEY = new SerializableDataType<>(
        TimedKey.class,
        TIMED_KEY::send,
        TIMED_KEY::receive,
        jsonElement -> {
            if (!(jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString())) return TIMED_KEY.read(jsonElement);
            else {

                TimedKey timedKey = new TimedKey();
                timedKey.key = jsonPrimitive.getAsString();

                return timedKey;

            }
        }
    );

    public static final SerializableDataType<FunctionalKey> FUNCTIONAL_KEY = SerializableDataType.compound(
        FunctionalKey.class,
        new SerializableData()
            .add("key", SerializableDataTypes.STRING)
            .add("continuous", SerializableDataTypes.BOOLEAN, false)
            .add("action", ApoliDataTypes.ENTITY_ACTION, null),
        data -> new FunctionalKey(
            data.getString("key"),
            data.getBoolean("continuous"),
            data.get("action")
        ),
        (serializableData, functionalKey) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("key", functionalKey.key);
            data.set("continuous", functionalKey.continuous);
            data.set("action", functionalKey.action);

            return data;

        }
    );

    public static final SerializableDataType<FunctionalKey> BACKWARDS_COMPATIBLE_FUNCTIONAL_KEY = new SerializableDataType<>(
        FunctionalKey.class,
        FUNCTIONAL_KEY::send,
        FUNCTIONAL_KEY::receive,
        jsonElement -> {
            if (!(jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString())) return FUNCTIONAL_KEY.read(jsonElement);
            else {

                FunctionalKey functionalKey = new FunctionalKey();
                functionalKey.key = jsonPrimitive.getAsString();

                return functionalKey;

            }
        }
    );

    public static final SerializableDataType<List<TimedKey>> TIMED_KEYS = SerializableDataType.list(TIMED_KEY);

    public static final SerializableDataType<List<FunctionalKey>> FUNCTIONAL_KEYS = SerializableDataType.list(FUNCTIONAL_KEY);

    public static final SerializableDataType<List<Key>> BACKWARDS_COMPATIBLE_KEYS = SerializableDataType.list(BACKWARDS_COMPATIBLE_KEY);

    public static final SerializableDataType<List<TimedKey>> BACKWARDS_COMPATIBLE_TIMED_KEYS = SerializableDataType.list(BACKWARDS_COMPATIBLE_TIMED_KEY);

    public static final SerializableDataType<List<FunctionalKey>> BACKWARDS_COMPATIBLE_FUNCTIONAL_KEYS = SerializableDataType.list(BACKWARDS_COMPATIBLE_FUNCTIONAL_KEY);

    public static final SerializableDataType<Pair<ArgumentWrapper<Integer>, ItemStack>> GENERALIZED_POSITIONED_ITEM_STACK = SerializableDataType.compound(
        ClassUtil.castClass(Pair.class),
        new SerializableData()
            .add("item", SerializableDataTypes.ITEM)
            .add("amount", SerializableDataTypes.INT, 1)
            .add("tag", SerializableDataTypes.NBT, null)
            .add("slot", ApoliDataTypes.ITEM_SLOT, null),
        data -> {

            ItemStack itemStack = new ItemStack((Item) data.get("item"), data.getInt("amount"));
            ArgumentWrapper<Integer> slotArgumentWrapper = data.get("slot");
            data.ifPresent("tag", itemStack::setNbt);

            return new Pair<>(slotArgumentWrapper, itemStack);

        },
        (serializableData, generalizedPositionedStack) -> {

            SerializableData.Instance data = serializableData.new Instance();
            ArgumentWrapper<Integer> slotArgumentWrapper = generalizedPositionedStack.getLeft();
            ItemStack itemStack = generalizedPositionedStack.getRight();

            data.set("item", itemStack.getItem());
            data.set("amount", itemStack.getCount());
            data.set("tag", itemStack.hasNbt() ? itemStack.getNbt() : null);
            data.set("slot", slotArgumentWrapper);

            return data;

        }
    );

    public static final SerializableDataType<List<Pair<ArgumentWrapper<Integer>, ItemStack>>> GENERALIZED_POSITIONED_ITEM_STACKS = SerializableDataType.list(GENERALIZED_POSITIONED_ITEM_STACK);

    public static final SerializableDataType<AbstractTeam.CollisionRule> ABSTRACT_TEAM_COLLISION_RULE = SerializableDataType.enumValue(AbstractTeam.CollisionRule.class);

    public static final SerializableDataType<AbstractTeam.VisibilityRule> ABSTRACT_TEAM_VISIBILITY_RULE = SerializableDataType.enumValue(AbstractTeam.VisibilityRule.class);

    public static final SerializableDataType<EggolibAbstractTeam> ABSTRACT_TEAM = SerializableDataType.compound(
        EggolibAbstractTeam.class,
        new SerializableData()
            .add("name", SerializableDataTypes.STRING, null)
            .add("friendly_fire", SerializableDataTypes.BOOLEAN, null)
            .add("show_friendly_invisibles", SerializableDataTypes.BOOLEAN, null)
            .add("nametag_visibility", EggolibDataTypes.ABSTRACT_TEAM_VISIBILITY_RULE, null)
            .add("death_message_visibility", EggolibDataTypes.ABSTRACT_TEAM_VISIBILITY_RULE, null)
            .add("collision_rule", EggolibDataTypes.ABSTRACT_TEAM_COLLISION_RULE, null),
        data -> {

            EggolibAbstractTeam eggolibAbstractTeam = new EggolibAbstractTeam();

            data.ifPresent("name", eggolibAbstractTeam::setName);
            data.ifPresent("friendly_fire", eggolibAbstractTeam::setFriendlyFireAllowed);
            data.ifPresent("show_friendly_invisibles", eggolibAbstractTeam::setShowFriendlyInvisibles);
            data.ifPresent("nametag_visibility", eggolibAbstractTeam::setNametagVisibilityRule);
            data.ifPresent("death_message_visibility", eggolibAbstractTeam::setDeathMessageVisibilityRule);
            data.ifPresent("collision_rule", eggolibAbstractTeam::setCollisionRule);

            return eggolibAbstractTeam;

        },
        (serializableData, eggolibAbstractTeam) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("name", eggolibAbstractTeam.getName());
            data.set("friendly_fire", eggolibAbstractTeam.isFriendlyFireAllowed());
            data.set("show_friendly_invisibles", eggolibAbstractTeam.shouldShowFriendlyInvisibles());
            data.set("nametag_visibility", eggolibAbstractTeam.getNametagVisibilityRule());
            data.set("death_message_visibility", eggolibAbstractTeam.getDeathMessageVisibilityRule());
            data.set("collision_rule", eggolibAbstractTeam.getCollisionRule());

            return data;

        }
    );

    public static final SerializableDataType<List<EggolibAbstractTeam>> ABSTRACT_TEAMS = SerializableDataType.list(ABSTRACT_TEAM);

    public static final SerializableDataType<EggolibAbstractTeam> BACKWARDS_COMPATIBLE_ABSTRACT_TEAM = new SerializableDataType<>(
        EggolibAbstractTeam.class,
        ABSTRACT_TEAM::send,
        ABSTRACT_TEAM::receive,
        jsonElement -> {
            if (!(jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString())) return ABSTRACT_TEAM.read(jsonElement);
            else {
                EggolibAbstractTeam eggolibAbstractTeam = new EggolibAbstractTeam();
                eggolibAbstractTeam.setName(jsonPrimitive.getAsString());
                return eggolibAbstractTeam;
            }
        }
    );

    public static final SerializableDataType<List<EggolibAbstractTeam>> BACKWARDS_COMPATIBLE_ABSTRACT_TEAMS = SerializableDataType.list(BACKWARDS_COMPATIBLE_ABSTRACT_TEAM);

}