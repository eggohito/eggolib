package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import org.joml.Vector3f;

import java.util.List;
import java.util.function.Predicate;

public class ModifyRidingPositionPower extends ModifyMountingPositionPower {

	public ModifyRidingPositionPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<Entity, Entity>> biEntityCondition, List<Modifier> xModifiers, List<Modifier> yModifiers, List<Modifier> zModifiers) {
		super(type, entity, biEntityCondition, xModifiers, yModifiers, zModifiers);
	}

	@Override
	public boolean doesApply(Entity vehicle) {
		return biEntityCondition == null || biEntityCondition.test(new Pair<>(entity, vehicle));
	}

	public static Vector3f modify(Entity actor, Entity target, Vector3f original) {
		return modify(actor, target, original, ModifyRidingPositionPower.class);
	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("modify_riding_position"), DATA,
			data -> (powerType, livingEntity) -> new ModifyRidingPositionPower(
				powerType,
				livingEntity,
				data.get("bientity_condition"),
				data.get("x_modifier"),
				data.get("y_modifier"),
				data.get("z_modifier")
			)
		).allowCondition();
	}

}
