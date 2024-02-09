package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.calio.data.SerializableData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import org.joml.Vector3f;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class ModifyMountingPositionPower extends Power {

	public static final SerializableData DATA = new SerializableData()
		.add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
		.add("x_modifier", Modifier.LIST_TYPE, new LinkedList<>())
		.add("y_modifier", Modifier.LIST_TYPE, new LinkedList<>())
		.add("z_modifier", Modifier.LIST_TYPE, new LinkedList<>());

	protected final Predicate<Pair<Entity, Entity>> biEntityCondition;
	protected final Map<Direction.Axis, List<Modifier>> positionModifiers;

	public ModifyMountingPositionPower(PowerType<?> type, LivingEntity entity, Predicate<Pair<Entity, Entity>> biEntityCondition, List<Modifier> xModifiers, List<Modifier> yModifiers, List<Modifier> zModifiers) {
		super(type, entity);

		this.biEntityCondition = biEntityCondition;
		this.positionModifiers = new EnumMap<>(Direction.Axis.class);

		this.positionModifiers.put(Direction.Axis.X, xModifiers);
		this.positionModifiers.put(Direction.Axis.Y, yModifiers);
		this.positionModifiers.put(Direction.Axis.Z, zModifiers);

	}

	public Map<Direction.Axis, List<Modifier>> getPositionModifiers() {
		return positionModifiers;
	}

	public abstract boolean doesApply(Entity target);

	protected static <T extends ModifyMountingPositionPower> Vector3f modify(Entity actor, Entity target, Vector3f original, Class<T> powerClass) {

		Map<Direction.Axis, List<Modifier>> modifiers = PowerHolderComponent.getPowers(actor, powerClass)
			.stream()
			.filter(p -> p.doesApply(target))
			.flatMap(p -> p.getPositionModifiers().entrySet().stream())
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> {
				o1.addAll(o2);
				return o1;
			}, LinkedHashMap::new));

		if (modifiers.isEmpty()) {
			return original;
		}

		return new Vector3f(
			(float) ModifierUtil.applyModifiers(target, modifiers.get(Direction.Axis.X), original.x()),
			(float) ModifierUtil.applyModifiers(target, modifiers.get(Direction.Axis.Y), original.y()),
			(float) ModifierUtil.applyModifiers(target, modifiers.get(Direction.Axis.Z), original.z())
		);

	}

}
