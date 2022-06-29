package io.github.eggohito.eggolib.data;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.util.*;
import net.minecraft.command.argument.ItemSlotArgumentType;
import net.minecraft.util.Pair;

import java.util.EnumSet;
import java.util.List;

import static io.github.eggohito.eggolib.util.EggolibMathUtil.*;
import static io.github.eggohito.eggolib.util.EggolibInventoryUtil.*;

public class EggolibDataTypes {

    public static final SerializableDataType<MathOperation> MATH_OPERATION = SerializableDataType.enumValue(EggolibMathUtil.MathOperation.class);

    public static final SerializableDataType<InventoryType> INVENTORY_TYPE = SerializableDataType.enumValue(EggolibInventoryUtil.InventoryType.class);

    public static final SerializableDataType<EggolibToolType> TOOL_TYPE = SerializableDataType.enumValue(EggolibToolType.class);

    public static final SerializableDataType<EnumSet<EggolibToolType>> TOOL_TYPE_SET = SerializableDataType.enumSet(EggolibToolType.class, TOOL_TYPE);

    public static final SerializableDataType<EggolibPerspective> PERSPECTIVE = SerializableDataType.enumValue(EggolibPerspective.class);

    public static final SerializableDataType<EnumSet<EggolibPerspective>> PERSPECTIVE_SET = SerializableDataType.enumSet(EggolibPerspective.class, PERSPECTIVE);

    public static final SerializableDataType<EggolibArgumentWrapper<Integer>> ITEM_SLOT = EggolibSerializableDataType.argumentType(ItemSlotArgumentType.itemSlot());

    public static final SerializableDataType<List<EggolibArgumentWrapper<Integer>>> ITEM_SLOTS = SerializableDataType.list(ITEM_SLOT);

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

}