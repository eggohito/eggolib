package io.github.eggohito.eggolib.data;

import io.github.apace100.calio.data.SerializableDataType;
import io.github.eggohito.eggolib.util.EggolibArgumentWrapper;
import io.github.eggohito.eggolib.util.*;
import net.minecraft.command.argument.ItemSlotArgumentType;

import java.util.EnumSet;
import java.util.List;

public class EggolibDataTypes {

    public static final SerializableDataType<EggolibMathUtil.MathOperation> MATH_OPERATION = SerializableDataType.enumValue(EggolibMathUtil.MathOperation.class);

    public static final SerializableDataType<InventoryUtil.InventoryType> INVENTORY_TYPE = SerializableDataType.enumValue(InventoryUtil.InventoryType.class);

    public static final SerializableDataType<EggolibToolType> TOOL_TYPE = SerializableDataType.enumValue(EggolibToolType.class);

    public static final SerializableDataType<EnumSet<EggolibToolType>> TOOL_TYPE_SET = SerializableDataType.enumSet(EggolibToolType.class, TOOL_TYPE);

    public static final SerializableDataType<EggolibPerspective> PERSPECTIVE = SerializableDataType.enumValue(EggolibPerspective.class);

    public static final SerializableDataType<EnumSet<EggolibPerspective>> PERSPECTIVE_SET = SerializableDataType.enumSet(EggolibPerspective.class, PERSPECTIVE);

    public static final SerializableDataType<EggolibArgumentWrapper<Integer>> ITEM_SLOT = EggolibSerializableDataType.argumentType(ItemSlotArgumentType.itemSlot());

    public static final SerializableDataType<List<EggolibArgumentWrapper<Integer>>> ITEM_SLOTS = SerializableDataType.list(ITEM_SLOT);

}