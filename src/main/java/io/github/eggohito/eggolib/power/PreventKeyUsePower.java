package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.mixin.KeyBindingAccessor;
import io.github.eggohito.eggolib.util.Key;
import io.github.eggohito.eggolib.util.key.FunctionalKey;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class PreventKeyUsePower extends Power {

	private final List<FunctionalKey> keys;
	private final List<Key> specifiedKeys;

	public PreventKeyUsePower(PowerType<?> powerType, LivingEntity livingEntity, FunctionalKey key, List<FunctionalKey> keys) {

		super(powerType, livingEntity);

		this.keys = new LinkedList<>();
		this.specifiedKeys = new LinkedList<>();

		if (key != null) {
			this.keys.add(key);
		}
		if (keys != null) {
			this.keys.addAll(keys);
		}

	}

	@SuppressWarnings("ConstantValue")
	@Environment(EnvType.CLIENT)
	public boolean doesApply(KeyBinding keyBinding) {
		return specifiedKeys
			.stream()
			.map(k -> KeyBindingAccessor.getIdAndKeyMap().get(k.key))
			.anyMatch(kb -> Objects.equals(kb, keyBinding));
	}

	public void executeActions(String key) {
		keys
			.stream()
			.filter(k -> k.key.equals(key))
			.forEach(k -> k.function(entity));
	}

	public List<Key> getSpecifiedKeys() {
		return specifiedKeys;
	}

	@Override
	public NbtElement toTag() {

		NbtCompound rootNbt = new NbtCompound();
		NbtList keysNbt = new NbtList();

		for (FunctionalKey key : keys) {

			NbtCompound keyNbt = new NbtCompound();

			keyNbt.putString("Key", key.key);
			keyNbt.putBoolean("Continuous", key.continuous);

			keysNbt.add(keyNbt);

		}

		rootNbt.put("Keys", keysNbt);
		return rootNbt;

	}

	@Override
	public void fromTag(NbtElement tag) {

		if (!(tag instanceof NbtCompound rootNbt)) {
			return;
		}

		specifiedKeys.clear();
		NbtList keysNbt = rootNbt.getList("Keys", NbtElement.COMPOUND_TYPE);

		for (int i = 0; i < keysNbt.size(); i++) {

			NbtCompound keyNbt = keysNbt.getCompound(i);
			Key specifiedKey = new Key(keyNbt.getString("Key"), keyNbt.getBoolean("Continuous"));

			specifiedKeys.add(specifiedKey);

		}

	}

	public static PowerFactory<?> getFactory() {
		return new PowerFactory<>(
			Eggolib.identifier("prevent_key_use"),
			new SerializableData()
				.add("key", EggolibDataTypes.BACKWARDS_COMPATIBLE_FUNCTIONAL_KEY, null)
				.add("keys", EggolibDataTypes.BACKWARDS_COMPATIBLE_FUNCTIONAL_KEYS, null),
			data -> (powerType, livingEntity) -> new PreventKeyUsePower(
				powerType,
				livingEntity,
				data.get("key"),
				data.get("keys")
			)
		).allowCondition();
	}

}
