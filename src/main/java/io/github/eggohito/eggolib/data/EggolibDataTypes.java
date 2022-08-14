package io.github.eggohito.eggolib.data;

import com.google.gson.JsonPrimitive;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.util.*;
import io.github.eggohito.eggolib.util.EggolibMathUtil.MathOperation;
import net.minecraft.util.Pair;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.List;

public class EggolibDataTypes {

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

    public static final SerializableDataType<Key.Timed> TIMED_KEY = SerializableDataType.compound(
        Key.Timed.class,
        new SerializableData()
            .add("key", SerializableDataTypes.STRING)
            .add("ticks", SerializableDataTypes.INT),
        data -> new Key.Timed(data.getString("key"), data.getInt("ticks")),
        (serializableData, timedKey) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("key", timedKey.key);
            data.set("ticks", timedKey.ticks);

            return data;

        }
    );

    public static final SerializableDataType<Key.Functional> FUNCTIONAL_KEY = SerializableDataType.compound(
        Key.Functional.class,
        new SerializableData()
            .add("key", SerializableDataTypes.STRING)
            .add("continuous", SerializableDataTypes.BOOLEAN, false)
            .add("action", ApoliDataTypes.ENTITY_ACTION, null),
        data -> new Key.Functional(
            data.getString("key"),
            data.getBoolean("continuous"),
            data.get("action")
        ),
        (serializableData, functional) -> {

            SerializableData.Instance data = serializableData.new Instance();

            data.set("key", functional.key);
            data.set("continuous", functional.continuous);
            data.set("action", functional.action);

            return data;

        }
    );

    public static final SerializableDataType<Key.Functional> BACKWARDS_COMPATIBLE_FUNCTIONAL_KEY = new SerializableDataType<>(
        Key.Functional.class,
        FUNCTIONAL_KEY::send,
        FUNCTIONAL_KEY::receive,
        jsonElement -> {

            if (!(jsonElement instanceof JsonPrimitive jsonPrimitive && jsonPrimitive.isString())) return FUNCTIONAL_KEY.read(jsonElement);
            return new Key.Functional(
                jsonPrimitive.getAsString(),
                false,
                null
            );

        }
    );

    public static final SerializableDataType<List<Key.Timed>> TIMED_KEYS = SerializableDataType.list(TIMED_KEY);

    public static final SerializableDataType<List<Key.Functional>> FUNCTIONAL_KEYS = SerializableDataType.list(FUNCTIONAL_KEY);

    public static final SerializableDataType<List<Key.Functional>> BACKWARDS_COMPATIBLE_FUNCTIONAL_KEYS = SerializableDataType.list(BACKWARDS_COMPATIBLE_FUNCTIONAL_KEY);

}