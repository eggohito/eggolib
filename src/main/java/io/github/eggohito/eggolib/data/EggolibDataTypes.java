package io.github.eggohito.eggolib.data;

import com.google.gson.JsonPrimitive;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.SerializationHelper;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.condition.EggolibConditionTypes;
import io.github.eggohito.eggolib.util.*;
import io.github.eggohito.eggolib.util.MathUtil.MathOperation;
import io.github.eggohito.eggolib.util.key.FunctionalKey;
import io.github.eggohito.eggolib.util.key.TimedKey;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ScoreHolderArgumentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.scoreboard.AbstractTeam;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Pair;
import net.minecraft.world.dimension.DimensionType;

import java.util.EnumSet;
import java.util.List;

@SuppressWarnings("unused")
public class EggolibDataTypes {

    public static final SerializableDataType<ConditionFactory<RegistryEntry<DimensionType>>.Instance> DIMENSION_TYPE_CONDITION = new SerializableDataType<>(
        ClassUtil.castClass(ConditionFactory.Instance.class),
        EggolibConditionTypes.DIMENSION_TYPE::write,
        EggolibConditionTypes.DIMENSION_TYPE::read,
        EggolibConditionTypes.DIMENSION_TYPE::read
    );

    public static final SerializableDataType<List<ConditionFactory<RegistryEntry<DimensionType>>.Instance>> DIMENSION_TYPE_CONDITIONS = SerializableDataType.list(DIMENSION_TYPE_CONDITION);

    public static final SerializableDataType<ArgumentWrapper<EntitySelector>> PLAYER_SELECTOR = SerializableDataType.argumentType(EntityArgumentType.player());

    public static final SerializableDataType<ArgumentWrapper<EntitySelector>> PLAYERS_SELECTOR = SerializableDataType.argumentType(EntityArgumentType.players());

    public static final SerializableDataType<ArgumentWrapper<EntitySelector>> ENTITY_SELECTOR = SerializableDataType.argumentType(EntityArgumentType.entity());

    public static final SerializableDataType<ArgumentWrapper<EntitySelector>> ENTITIES_SELECTOR = SerializableDataType.argumentType(EntityArgumentType.entities());

    public static final SerializableDataType<ArgumentWrapper<ScoreHolderArgumentType.ScoreHolder>> SCORE_HOLDER = SerializableDataType.argumentType(ScoreHolderArgumentType.scoreHolder());

    public static final SerializableDataType<ArgumentWrapper<ScoreHolderArgumentType.ScoreHolder>> SCORE_HOLDERS = SerializableDataType.argumentType(ScoreHolderArgumentType.scoreHolders());

    public static final SerializableDataType<MathOperation> MATH_OPERATION = SerializableDataType.enumValue(MathUtil.MathOperation.class);

    public static final SerializableDataType<PowerOperation> POWER_OPERATION = SerializableDataType.enumValue(PowerOperation.class, SerializationHelper.buildEnumMap(PowerOperation.class, PowerOperation::getOperationName));

    public static final SerializableDataType<ToolType> TOOL_TYPE = SerializableDataType.enumValue(ToolType.class);

    public static final SerializableDataType<EnumSet<ToolType>> TOOL_TYPE_SET = SerializableDataType.enumSet(ToolType.class, TOOL_TYPE);

    public static final SerializableDataType<EggolibPerspective> PERSPECTIVE = SerializableDataType.enumValue(EggolibPerspective.class);

    public static final SerializableDataType<EnumSet<EggolibPerspective>> PERSPECTIVE_SET = SerializableDataType.enumSet(EggolibPerspective.class, PERSPECTIVE);

    public static final SerializableDataType<AbstractTeam.CollisionRule> COLLISION_RULE = SerializableDataType.enumValue(AbstractTeam.CollisionRule.class);

    public static final SerializableDataType<AbstractTeam.VisibilityRule> VISIBILITY_RULE = SerializableDataType.enumValue(AbstractTeam.VisibilityRule.class);

    public static final SerializableDataType<SoundCategory> SOUND_CATEGORY = SerializableDataType.enumValue(SoundCategory.class);

    public static final SerializableDataType<Pair<ArgumentWrapper<ScoreHolderArgumentType.ScoreHolder>, String>> SCOREBOARD = SerializableDataType.compound(
        ClassUtil.castClass(Pair.class),
        new SerializableData()
            .add("name", EggolibDataTypes.SCORE_HOLDER)
            .add("objective", SerializableDataTypes.STRING),
        data -> new Pair<>(
            data.get("name"),
            data.get("objective")
        ),
        (serializableData, scoreHolderAndObjectiveName) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("name", scoreHolderAndObjectiveName.getLeft());
            data.set("objective", scoreHolderAndObjectiveName.getRight());

            return data;

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

    public static final SerializableDataType<EggolibTeam> TEAM = SerializableDataType.compound(
        EggolibTeam.class,
        new SerializableData()
            .add("name", SerializableDataTypes.STRING, null)
            .add("friendly_fire", SerializableDataTypes.BOOLEAN, null)
            .add("show_friendly_invisibles", SerializableDataTypes.BOOLEAN, null)
            .add("nametag_visibility", EggolibDataTypes.VISIBILITY_RULE, null)
            .add("death_message_visibility", EggolibDataTypes.VISIBILITY_RULE, null)
            .add("collision_rule", EggolibDataTypes.COLLISION_RULE, null),
        data -> {

            EggolibTeam eggolibTeam = new EggolibTeam();

            data.ifPresent("name", eggolibTeam::setName);
            data.ifPresent("friendly_fire", eggolibTeam::friendlyFireAllowed);
            data.ifPresent("show_friendly_invisibles", eggolibTeam::showFriendlyInvisibles);
            data.ifPresent("nametag_visibility", eggolibTeam::setNameTagVisibilityRule);
            data.ifPresent("death_message_visibility", eggolibTeam::setDeathMessageVisibilityRule);
            data.ifPresent("collision_rule", eggolibTeam::setCollisionRule);

            return eggolibTeam;

        },
        (serializableData, eggolibTeam) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("name", eggolibTeam.getName());
            data.set("friendly_fire", eggolibTeam.isFriendlyFireAllowed());
            data.set("show_friendly_invisibles", eggolibTeam.shouldShowFriendlyInvisibles());
            data.set("nametag_visibility", eggolibTeam.getNameTagVisibilityRule());
            data.set("death_message_visibility", eggolibTeam.getDeathMessageVisibilityRule());
            data.set("collision_rule", eggolibTeam.getCollisionRule());

            return data;

        }
    );

    public static final SerializableDataType<List<EggolibTeam>> TEAMS = SerializableDataType.list(TEAM);

    public static final SerializableDataType<EggolibTeam> BACKWARDS_COMPATIBLE_TEAM = new SerializableDataType<>(
        EggolibTeam.class,
        TEAM::send,
        TEAM::receive,
        jsonElement -> {
            if (!(jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString())) return TEAM.read(jsonElement);
            else {
                EggolibTeam eggolibTeam = new EggolibTeam();
                eggolibTeam.setName(jsonPrimitive.getAsString());
                return eggolibTeam;
            }
        }
    );

    public static final SerializableDataType<List<EggolibTeam>> BACKWARDS_COMPATIBLE_TEAMS = SerializableDataType.list(BACKWARDS_COMPATIBLE_TEAM);

    public static final SerializableDataType<Integer> POSITIVE_INT = new SerializableDataType<>(
        Integer.class,
        PacketByteBuf::writeInt,
        PacketByteBuf::readInt,
        jsonElement -> {

            int value = jsonElement.getAsInt();

            if (value <= 0) throw new IllegalArgumentException("'" + value + "' must be equal to or greater than 1!");
            else return value;

        }
    );

    public static final SerializableDataType<List<Integer>> POSITIVE_INTS = SerializableDataType.list(POSITIVE_INT);

}
