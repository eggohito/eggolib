package io.github.eggohito.eggolib.condition.damage;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Pair;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class EggolibProjectileCondition {

    public static boolean condition(SerializableData.Instance data, Pair<DamageSource, Float> damageSourceAndAmount) {

        DamageSource damageSource = damageSourceAndAmount.getLeft();
        if (!(damageSource.isIn(DamageTypeTags.IS_PROJECTILE))) return false;

        Entity projectileEntity = damageSource.getSource();
        if (projectileEntity == null) return false;

        NbtCompound projectileNbt = new NbtCompound();
        projectileEntity.writeNbt(projectileNbt);

        NbtCompound specifiedNbt = data.get("nbt");
        Predicate<Entity> projectileCondition = data.get("projectile_condition");
        Set<EntityType<?>> specifiedEntityTypes = new HashSet<>();

        if (data.isPresent("projectile")) specifiedEntityTypes.add(data.get("projectile"));
        if (data.isPresent("projectiles")) specifiedEntityTypes.addAll(data.get("projectiles"));

        return specifiedEntityTypes.stream().anyMatch(projectileEntity.getType()::equals)
            && (projectileCondition == null || projectileCondition.test(projectileEntity))
            && (specifiedNbt == null || NbtHelper.matches(specifiedNbt, projectileNbt, true));

    }

    public static ConditionFactory<Pair<DamageSource, Float>> getFactory() {
        return new ConditionFactory<>(
            Eggolib.identifier("projectile"),
            new SerializableData()
                .add("projectile", SerializableDataTypes.ENTITY_TYPE, null)
                .add("projectiles", SerializableDataType.list(SerializableDataTypes.ENTITY_TYPE), null)
                .add("projectile_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("nbt", SerializableDataTypes.NBT, null),
            EggolibProjectileCondition::condition
        );
    }

}
