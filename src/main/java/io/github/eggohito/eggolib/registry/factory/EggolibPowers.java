package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.power.*;
import net.minecraft.registry.Registry;

public class EggolibPowers {

	public static void register() {
		register(ActionOnBlockHitPower.getFactory());
		register(ActionOnBlockPlacePower.getFactory());
		register(ActionOnCriticalHitPower.getFactory());
		register(ActionOnItemPickupPower.getFactory());
		register(ActionOnKeySequencePower.getFactory());
		register(CrawlingPower.getFactory());
		register(InvisibilityPower.getFactory());
		register(StartingEquipmentPower.getFactory());
		register(StatPower.getFactory());
		register(TooltipPower.getFactory());
		register(GameEventListenerPower.getFactory());
		register(ModelFlipPower.getFactory());
		register(ModifyBouncinessPower.getFactory());
		register(ModifyBreathingPower.getFactory());
		register(ModifyFovPower.getFactory());
		register(ModifyHurtTicksPower.getFactory());
		register(ModifyLabelRenderPower.getFactory());
		register(ModifyMouseSensitivityPower.getFactory());
		register(PreventBlockPlacePower.getFactory());
		register(PreventCriticalHitPower.getFactory());
		register(PreventKeyUsePower.getFactory());
		register(PreventItemPickupPower.getFactory());
	}

	public static PowerFactory<?> register(PowerFactory<?> powerFactory) {
		return Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
	}

}
