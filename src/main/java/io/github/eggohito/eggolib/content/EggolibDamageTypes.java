package io.github.eggohito.eggolib.content;

import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public interface EggolibDamageTypes {

	RegistryKey<DamageType> CHANGE_HEALTH_UNDERFLOW = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Eggolib.identifier("change_health_underflow"));

}
