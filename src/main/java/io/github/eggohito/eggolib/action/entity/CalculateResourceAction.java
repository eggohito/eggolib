package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.PowerOperation;
import net.minecraft.entity.Entity;

public class CalculateResourceAction {

	public static void action(SerializableData.Instance data, Entity entity) {

		PowerHolderComponent component = PowerHolderComponent.KEY.maybeGet(entity).orElse(null);
		if (component == null) {
			return;
		}

		PowerType<?> targetPowerType = data.get("target");
		if (targetPowerType == null) {
			return;
		}

		Power targetPower = component.getPower(targetPowerType);
		if (targetPower == null) {
			return;
		}

		PowerType<?> sourcePowerType = data.get("source");
		if (sourcePowerType == null) {
			return;
		}

		Power sourcePower = component.getPower(sourcePowerType);
		if (sourcePower == null) {
			return;
		}

		PowerOperation powerOperation = data.get("operation");
		powerOperation.operate(targetPower, sourcePower);

	}

	public static ActionFactory<Entity> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("calculate_resource"),
			new SerializableData()
				.add("target", ApoliDataTypes.POWER_TYPE)
				.add("operation", EggolibDataTypes.POWER_OPERATION, PowerOperation.ADD)
				.add("source", ApoliDataTypes.POWER_TYPE),
			CalculateResourceAction::action
		);
	}

}
