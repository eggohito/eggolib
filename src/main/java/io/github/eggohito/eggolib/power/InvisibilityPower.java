package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;

import java.util.function.Predicate;

public class InvisibilityPower extends io.github.apace100.apoli.power.InvisibilityPower {

	private final Predicate<Pair<Entity, Entity>> biEntityCondition;

	public InvisibilityPower(PowerType<?> powerType, LivingEntity livingEntity, Predicate<Pair<Entity, Entity>> biEntityCondition, boolean renderArmor, boolean renderOutline) {
		super(powerType, livingEntity, renderArmor, renderOutline);
		this.biEntityCondition = biEntityCondition;
	}

	public boolean doesApply(PlayerEntity viewer) {
		return biEntityCondition == null || biEntityCondition.test(new Pair<>(viewer, entity));
	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("invisibility"),
			new SerializableData()
				.add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
				.add("render_armor", SerializableDataTypes.BOOLEAN, false)
				.add("render_outline", SerializableDataTypes.BOOLEAN, false),
			data -> (powerType, livingEntity) -> new InvisibilityPower(
				powerType,
				livingEntity,
				data.get("bientity_condition"),
				data.get("render_armor"),
				data.get("render_outline")
			)
		).allowCondition();
	}

}
