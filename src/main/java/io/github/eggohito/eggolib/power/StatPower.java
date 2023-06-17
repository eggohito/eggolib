package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ResourcePower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.stat.Stat;

import java.util.function.Consumer;

public class StatPower extends ResourcePower {

	private final Stat<?> stat;
	private final Consumer<Entity> onChangeAction;

	public StatPower(PowerType<?> powerType, LivingEntity livingEntity, Stat<?> stat, Consumer<Entity> onChangeAction, Consumer<Entity> minAction, Consumer<Entity> maxAction, HudRender hudRender, int min, int max, int startValue) {
		super(powerType, livingEntity, hudRender, startValue, min, max, minAction, maxAction);
		this.stat = stat;
		this.onChangeAction = onChangeAction;
	}

	public void increaseValue(Stat<?> stat, int amount) {
		if (this.stat.equals(stat)) {
			setValue(getValue() + amount);
			PowerHolderComponent.syncPower(entity, this.getType());
		}
	}

	@Override
	public int setValue(int newValue) {

		int currentValue = getValue();
		int supposedNewValue = super.setValue(newValue);

		if (currentValue != supposedNewValue && onChangeAction != null) {
			onChangeAction.accept(entity);
		}

		return supposedNewValue;

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("stat"),
			new SerializableData()
				.add("stat", SerializableDataTypes.STAT)
				.add("on_change_action", ApoliDataTypes.ENTITY_ACTION, null)
				.add("min_action", ApoliDataTypes.ENTITY_ACTION, null)
				.add("max_action", ApoliDataTypes.ENTITY_ACTION, null)
				.add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
				.add("min", SerializableDataTypes.INT)
				.add("max", SerializableDataTypes.INT)
				.addFunctionedDefault("start_value", SerializableDataTypes.INT, data -> data.get("min")),
			data -> (powerType, livingEntity) -> new StatPower(
				powerType,
				livingEntity,
				data.get("stat"),
				data.get("on_change_action"),
				data.get("min_action"),
				data.get("max_action"),
				data.get("hud_render"),
				data.get("min"),
				data.get("max"),
				data.get("start_value")
			)
		).allowCondition();
	}

}
