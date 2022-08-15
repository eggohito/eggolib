package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.Key;
import io.github.eggohito.eggolib.util.key.FunctionalKey;
import io.github.eggohito.eggolib.util.key.TimedKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 *  TODO: Use {@link TimedKey} for the sequences and implement the timing system.
 */
public class ActionOnKeySequencePower extends CooldownPower {

    private final List<Key> currentKeySequence = new ArrayList<>();

    private final List<Key> specifiedKeySequence;
    private final List<FunctionalKey> keys;
    private final Consumer<Entity> successAction;
    private final Consumer<Entity> failAction;

    public ActionOnKeySequencePower(PowerType<?> type, LivingEntity entity, Consumer<Entity> successAction, Consumer<Entity> failAction, int cooldownDuration, HudRender hudRender, List<FunctionalKey> keys, List<Key> keySequence) {
        super(type, entity, cooldownDuration, hudRender);
        this.successAction = successAction;
        this.failAction = failAction;
        this.keys = keys;
        this.specifiedKeySequence = keySequence;
    }

    public void onSuccess() {
        if (successAction != null) successAction.accept(entity);
        currentKeySequence.clear();
        use();
    }

    public void onFail() {
        if (failAction != null) failAction.accept(entity);
        currentKeySequence.clear();
    }

    public List<Key> getSpecifiedKeySequence() {
        return specifiedKeySequence;
    }

    public List<Key> getCurrentKeySequence() {
        return currentKeySequence;
    }

    public void addKeyToSequence(Key key) {

        if (!(canUse() && currentKeySequence.size() < specifiedKeySequence.size())) return;

        keys.stream().filter(key::equals).forEach(functionalKey -> functionalKey.function(entity));
        currentKeySequence.add(key);

    }

    public List<FunctionalKey> getKeys() {
        return keys;
    }

    @Override
    public NbtElement toTag() {

        NbtList specifiedKeySequenceNbtList = new NbtList();
        NbtList currentKeySequenceNbtList = new NbtList();
        NbtCompound rootNbtCompound = new NbtCompound();

        for (Key key : specifiedKeySequence) {
            NbtCompound keyNbtCompound = new NbtCompound();
            keyNbtCompound.putString("Key", key.key);
            specifiedKeySequenceNbtList.add(keyNbtCompound);
        }

        for (Key key : currentKeySequence) {
            NbtCompound keyNbtCompound = new NbtCompound();
            keyNbtCompound.putString("Key", key.key);
            currentKeySequenceNbtList.add(keyNbtCompound);
        }

        rootNbtCompound.put("SpecifiedKeySequence", specifiedKeySequenceNbtList);
        rootNbtCompound.put("CurrentKeySequence", currentKeySequenceNbtList);

        return rootNbtCompound;

    }

    @Override
    public void fromTag(NbtElement tag) {

        if (!(tag instanceof NbtCompound nbtCompound)) return;

        NbtList specifiedKeySequenceNbtList = nbtCompound.getList("SpecifiedKeySequence", NbtElement.COMPOUND_TYPE);
        NbtList currentKeySequenceNbtList = nbtCompound.getList("CurrentKeySequence", NbtElement.COMPOUND_TYPE);

        specifiedKeySequence.clear();
        for (NbtElement nbtElement : specifiedKeySequenceNbtList) {
            if (!(nbtElement instanceof NbtCompound keyNbtCompound)) continue;
            specifiedKeySequence.add(new Key(keyNbtCompound.getString("Key")));
        }

        currentKeySequence.clear();
        for (NbtElement nbtElement : currentKeySequenceNbtList) {
            if (!(nbtElement instanceof NbtCompound keyNbtCompound)) continue;
            currentKeySequence.add(new Key(keyNbtCompound.getString("Key")));
        }

    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("action_on_key_sequence"),
            new SerializableData()
                .add("success_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("fail_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("cooldown", SerializableDataTypes.INT, 0)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
                .add("keys", EggolibDataTypes.BACKWARDS_COMPATIBLE_FUNCTIONAL_KEYS)
                .add("key_sequence", EggolibDataTypes.BACKWARDS_COMPATIBLE_KEYS),
            data -> (powerType, livingEntity) -> new ActionOnKeySequencePower(
                powerType,
                livingEntity,
                data.get("success_action"),
                data.get("fail_action"),
                data.getInt("cooldown"),
                data.get("hud_render"),
                data.get("keys"),
                data.get("key_sequence")
            )
        ).allowCondition();
    }

}
