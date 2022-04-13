package io.github.eggohito.eggolib.data;

import io.github.apace100.calio.data.SerializableDataType;
import io.github.eggohito.eggolib.util.InventoryUtils;
import io.github.eggohito.eggolib.util.MathUtils;
import io.github.eggohito.eggolib.util.ToolType;

import java.util.EnumSet;

public class EggolibDataTypes {

    public static final SerializableDataType<MathUtils.Operation> MATH_OPERATION =
        SerializableDataType.enumValue(MathUtils.Operation.class);

    public static final SerializableDataType<InventoryUtils.InventoryType> INVENTORY_TYPE =
        SerializableDataType.enumValue(InventoryUtils.InventoryType.class);

    public static final SerializableDataType<ToolType> TOOL_TYPE = SerializableDataType.enumValue(ToolType.class);

    public static final SerializableDataType<EnumSet<ToolType>> TOOL_TYPE_SET = SerializableDataType.enumSet(ToolType.class, TOOL_TYPE);
}