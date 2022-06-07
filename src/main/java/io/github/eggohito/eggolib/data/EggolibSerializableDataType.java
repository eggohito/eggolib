package io.github.eggohito.eggolib.data;

import com.google.gson.JsonElement;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.util.EggolibArgumentWrapper;
import net.minecraft.network.PacketByteBuf;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EggolibSerializableDataType<T> extends SerializableDataType<T> {

    public EggolibSerializableDataType(Class<T> dataClass, BiConsumer<PacketByteBuf, T> send, Function<PacketByteBuf, T> receive, Function<JsonElement, T> read) {
        super(dataClass, send, receive, read);
    }

    public static <T, U extends ArgumentType<T>> SerializableDataType<EggolibArgumentWrapper<T>> argumentType(U argumentType) {

        return SerializableDataType.wrap(
            ClassUtil.castClass(EggolibArgumentWrapper.class),
            SerializableDataTypes.STRING,
            EggolibArgumentWrapper::rawArgument,
            s -> {
                try {
                    T t = argumentType.parse(new StringReader(s));
                    return new EggolibArgumentWrapper<>(t, s);
                }
                catch (CommandSyntaxException cse) {
                    throw new RuntimeException("Wrong syntax in argument type data", cse);
                }
            }
        );

    }

}
