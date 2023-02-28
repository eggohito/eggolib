package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.MiscUtilServer;
import io.github.eggohito.eggolib.util.EntityOffset;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.Optional;
import java.util.function.Consumer;

public class FireProjectileAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        if (!(entity.world instanceof ServerWorld serverWorld)) return;

        int count = data.get("count");
        for (int i = 0; i < count; i++) {

            EntityType<?> entityType = data.get("entity_type");
            NbtCompound entityNbt = data.get("tag");

            Optional<Entity> opt$entityToFire = MiscUtilServer.getEntityWithPassengers(
                serverWorld,
                entityType,
                entityNbt,
                EntityOffset.EYES.get(entity),
                entity.getYaw(),
                entity.getPitch()
            );

            if (opt$entityToFire.isEmpty()) return;
            Entity entityToFire = opt$entityToFire.get();

            float divergence = data.get("divergence");
            float speed = data.get("speed");

            float yaw = entity.getYaw();
            float pitch = entity.getPitch();

            Random random = serverWorld.getRandom();
            Vec3d rotationVector = entity.getRotationVector();

            if (entityToFire instanceof ProjectileEntity projectileEntity) {

                if (entityToFire instanceof ExplosiveProjectileEntity explosiveProjectileEntity) {
                    explosiveProjectileEntity.powerX = rotationVector.x * speed;
                    explosiveProjectileEntity.powerY = rotationVector.y * speed;
                    explosiveProjectileEntity.powerZ = rotationVector.z * speed;
                }

                projectileEntity.setOwner(entity);
                projectileEntity.setVelocity(entity, pitch, yaw, 0F, speed, divergence);

            }

            else {

                float f = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
                float g = -MathHelper.sin(pitch * 0.017453292F);
                float h =  MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);

                Vec3d vec3d = new Vec3d(f, g, h)
                    .normalize()
                    .add(random.nextGaussian() * 0.007499999832361937D * divergence, random.nextGaussian() * 0.007499999832361937D * divergence, random.nextGaussian() * 0.007499999832361937D * divergence)
                    .multiply(speed);

                entityToFire.setVelocity(vec3d);
                Vec3d entityVelocity = entity.getVelocity();
                entityToFire.addVelocity(entityVelocity.x, entity.isOnGround() ? 0.0D : entityVelocity.y, entityVelocity.z);

            }

            serverWorld.spawnNewEntityAndPassengers(entityToFire);
            data.<Consumer<Entity>>ifPresent("entity_action", entityAction -> entityAction.accept(entityToFire));

        }

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("fire_projectile"),
            new SerializableData()
                .add("entity_type", SerializableDataTypes.ENTITY_TYPE)
                .add("divergence", SerializableDataTypes.FLOAT, 1F)
                .add("speed", SerializableDataTypes.FLOAT, 1.5F)
                .add("count", EggolibDataTypes.POSITIVE_INT, 1)
                .add("tag", SerializableDataTypes.NBT, null)
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null),
            FireProjectileAction::action
        );
    }

}
