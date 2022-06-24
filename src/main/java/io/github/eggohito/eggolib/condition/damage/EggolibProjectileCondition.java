package io.github.eggohito.eggolib.condition.damage;

import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class EggolibProjectileCondition {

    public static boolean condition(SerializableData.Instance data, Pair<DamageSource, Float> damage) {

        if (!(damage.getLeft() instanceof ProjectileDamageSource projectileDamageSource)) return false;

        Entity projectileEntity = projectileDamageSource.getSource();
        if (projectileEntity == null) return false;

        NbtCompound projectileNbt = new NbtCompound();
        projectileEntity.writeNbt(projectileNbt);

        Set<EntityType<?>> specifiedEntityTypes = new HashSet<>();
        if (data.isPresent("projectile")) specifiedEntityTypes.add(data.get("projectile"));
        if (data.isPresent("projectiles")) specifiedEntityTypes.addAll(data.get("projectiles"));

        boolean bl = true;
        if (!specifiedEntityTypes.isEmpty()) bl = specifiedEntityTypes.stream().anyMatch(projectileEntity.getType()::equals);

        return bl && (!data.isPresent("nbt") || NbtHelper.matches(data.get("nbt"), projectileNbt, true));

    }

    public static ConditionFactory<Pair<DamageSource, Float>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("projectile"),
            new SerializableData()
                .add("projectile", SerializableDataTypes.ENTITY_TYPE, null)
                .add("projectiles", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE), null)
                .add("nbt", SerializableDataTypes.NBT, null),
            EggolibProjectileCondition::condition
        );
    }

}
