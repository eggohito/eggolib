package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.LivingEntity;

import java.util.List;

public class ModifyFovPower extends ValueModifyingPower {

	private final boolean affectedByFovEffectScale;

	public ModifyFovPower(PowerType<?> powerType, LivingEntity livingEntity, Modifier modifier, List<Modifier> modifiers, boolean affectedByFovScale) {
		super(powerType, livingEntity);

		if (modifier != null) {
			addModifier(modifier);
		}
		if (modifiers != null) {
			modifiers.forEach(this::addModifier);
		}

		this.affectedByFovEffectScale = affectedByFovScale;
	}

	public boolean isAffectedByFovEffectScale() {
		return affectedByFovEffectScale;
	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("modify_fov"),
			new SerializableData()
				.add("modifier", Modifier.DATA_TYPE, null)
				.add("modifiers", Modifier.LIST_TYPE, null)
				.add("affected_by_fov_effect_scale", SerializableDataTypes.BOOLEAN, true),
			data -> (powerType, livingEntity) -> new ModifyFovPower(
				powerType,
				livingEntity,
				data.get("modifier"),
				data.get("modifiers"),
				data.get("affected_by_fov_effect_scale")
			)
		).allowCondition();
	}

}
