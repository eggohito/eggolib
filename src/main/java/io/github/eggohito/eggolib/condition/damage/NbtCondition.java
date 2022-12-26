package io.github.eggohito.eggolib.condition.damage;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Pair;

public class NbtCondition {

    public static boolean condition(SerializableData.Instance data, Pair<DamageSource, Float> damageSourceAndAmount) {

        Entity entity = damageSourceAndAmount.getLeft().getSource();
        if (entity == null) return false;

        NbtCompound entityNbt = entity.writeNbt(new NbtCompound());
        return NbtHelper.matches(data.get("nbt"), entityNbt, true);

    }

    public static ConditionFactory<Pair<DamageSource, Float>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("nbt"),
            new SerializableData()
                .add("nbt", SerializableDataTypes.NBT),
            NbtCondition::condition
        );
    }

}
