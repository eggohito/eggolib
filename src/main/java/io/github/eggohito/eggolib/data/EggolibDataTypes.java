package io.github.eggohito.eggolib.data;

import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.util.*;
import net.minecraft.client.option.Perspective;
import net.minecraft.command.argument.ItemSlotArgumentType;

import java.util.EnumSet;
import java.util.List;

public class EggolibDataTypes {

    public static final SerializableDataType<MathUtil.MathOperation> MATH_OPERATION = SerializableDataType.enumValue(MathUtil.MathOperation.class);

    public static final SerializableDataType<InventoryUtil.InventoryType> INVENTORY_TYPE = SerializableDataType.enumValue(InventoryUtil.InventoryType.class);

    public static final SerializableDataType<ToolType> TOOL_TYPE = SerializableDataType.enumValue(ToolType.class);

    public static final SerializableDataType<EnumSet<ToolType>> TOOL_TYPE_SET = SerializableDataType.enumSet(ToolType.class, TOOL_TYPE);

    public static final SerializableDataType<Perspective> PERSPECTIVE = SerializableDataType.enumValue(Perspective.class);

    public static final SerializableDataType<EnumSet<Perspective>> PERSPECTIVE_SET = SerializableDataType.enumSet(Perspective.class, PERSPECTIVE);

    public static final SerializableDataType<ArgumentWrapper<Integer>> ITEM_SLOT = EggolibSerializableDataType.argumentType(ItemSlotArgumentType.itemSlot());

    public static final SerializableDataType<List<ArgumentWrapper<Integer>>> ITEM_SLOTS = SerializableDataType.list(ITEM_SLOT);

}