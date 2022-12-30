package io.github.eggohito.eggolib.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Function;

public enum EntityOffset {

    FEET(Entity::getPos),
    EYES(entity -> entity.getPos().add(0, entity.getEyeHeight(entity.getPose()), 0));

    final Function<Entity, Vec3d> function;

    EntityOffset(Function<Entity, Vec3d> function) {
        this.function = function;
    }

    public Vec3d get(Entity entity) {
        return function.apply(entity);
    }

}
