package io.github.eggohito.eggolib;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.compat.EggolibOriginsCompat;
import io.github.eggohito.eggolib.data.EggolibClassDataClient;
import io.github.eggohito.eggolib.networking.EggolibPackets;
import io.github.eggohito.eggolib.networking.EggolibPacketsS2C;
import io.github.eggohito.eggolib.power.ActionOnKeySequencePower;
import io.github.eggohito.eggolib.util.Key;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;

@Environment(EnvType.CLIENT)
public class EggolibClient implements ClientModInitializer {

    private static final HashMap<String, KeyBinding> ID_TO_KEYBINDING_MAP = new HashMap<>();
    private static HashMap<String, Boolean> previousKeyBindingStates = new HashMap<>();
    private static boolean initializedKeyBindingMap = false;

    @Override
    public void onInitializeClient() {

        //  Register the packets and the class data registries
        EggolibPacketsS2C.register();
        EggolibClassDataClient.register();

        //  Add Origins' screen classes to Eggolib's screen class data registry if it's loaded
        FabricLoader.getInstance().getModContainer("origins").ifPresent(EggolibOriginsCompat::init);

        //  Track which keybinds the player is pressing
        ClientTickEvents.START_CLIENT_TICK.register(
            minecraftClient -> {

                if (minecraftClient.player == null) return;

                List<ActionOnKeySequencePower> powers = PowerHolderComponent.getPowers(minecraftClient.player, ActionOnKeySequencePower.class);
                HashMap<ActionOnKeySequencePower, Key.Functional> triggeredPowers = new HashMap<>();
                HashMap<String, Boolean> currentKeyBindingStates = new HashMap<>();

                for (ActionOnKeySequencePower power : powers) {

                    List<Key.Functional> keys = power.getKeys();
                    for (Key.Functional key : keys) {

                        KeyBinding keyBinding = getKeyBinding(key.key);
                        if (keyBinding == null) continue;

                        currentKeyBindingStates.put(key.key, keyBinding.isPressed());
                        if (currentKeyBindingStates.get(key.key) && (key.continuous || !previousKeyBindingStates.getOrDefault(key.key, false))) triggeredPowers.put(power, key);

                    }

                }

                previousKeyBindingStates = currentKeyBindingStates;
                if (triggeredPowers.size() > 0) {
                    syncKeyPresses(triggeredPowers);
                    compareKeySequences(triggeredPowers);
                }

            }
        );

    }

    private void syncKeyPresses(HashMap<ActionOnKeySequencePower, Key.Functional> powerAndKeyMap) {

        PacketByteBuf syncKeyPressBuf = new PacketByteBuf(Unpooled.buffer());
        HashMap<Identifier, String> powerIdAndKeyStringMap = new HashMap<>();

        for (ActionOnKeySequencePower power : powerAndKeyMap.keySet()) {

            Key.Functional functionalKey = powerAndKeyMap.get(power);
            Key key = new Key(functionalKey.key);

            power.addKeyToSequence(key);
            powerIdAndKeyStringMap.put(power.getType().getIdentifier(), key.key);

        }

        int powerIdAndKeyMapSize = powerIdAndKeyStringMap.size();
        if (powerIdAndKeyMapSize <= 0) return;

        syncKeyPressBuf.writeInt(powerIdAndKeyMapSize);
        for (Identifier powerId : powerIdAndKeyStringMap.keySet()) {
            syncKeyPressBuf.writeIdentifier(powerId);
            syncKeyPressBuf.writeString(powerIdAndKeyStringMap.get(powerId));
        }

        ClientPlayNetworking.send(EggolibPackets.SYNC_KEY_PRESS, syncKeyPressBuf);

    }

    private void compareKeySequences(HashMap<ActionOnKeySequencePower, Key.Functional> powerAndKeyMap) {

        PacketByteBuf triggerKeySequenceBuf = new PacketByteBuf(Unpooled.buffer());
        HashMap<Identifier, Boolean> powerIdAndMatchingSequenceMap = new HashMap<>();

        for (ActionOnKeySequencePower power : powerAndKeyMap.keySet()) {

            List<String> specifiedKeySequence = power.getSpecifiedKeySequence().stream().map(key -> key.key).toList();
            List<String> currentKeySequence = power.getCurrentKeySequence().stream().map(key -> key.key).toList();

            if (specifiedKeySequence.equals(currentKeySequence)) {
                power.onSuccess();
                powerIdAndMatchingSequenceMap.put(power.getType().getIdentifier(), true);
            }

            else if (currentKeySequence.size() >= specifiedKeySequence.size()) {
                power.onFail();
                powerIdAndMatchingSequenceMap.put(power.getType().getIdentifier(), false);
            }

        }

        int powerIdAndMatchingSequenceMapSize = powerIdAndMatchingSequenceMap.size();
        if (powerIdAndMatchingSequenceMapSize <= 0) return;

        triggerKeySequenceBuf.writeInt(powerIdAndMatchingSequenceMapSize);
        for (Identifier powerId: powerIdAndMatchingSequenceMap.keySet()) {
            triggerKeySequenceBuf.writeIdentifier(powerId);
            triggerKeySequenceBuf.writeBoolean(powerIdAndMatchingSequenceMap.get(powerId));
        }

        ClientPlayNetworking.send(EggolibPackets.TRIGGER_KEY_SEQUENCE, triggerKeySequenceBuf);

    }

    private KeyBinding getKeyBinding(String name) {
        if (ID_TO_KEYBINDING_MAP.containsKey(name)) return ID_TO_KEYBINDING_MAP.get(name);
        else if (initializedKeyBindingMap) return null;
        else {

            initializedKeyBindingMap = true;

            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            KeyBinding[] keyBindings = minecraftClient.options.allKeys;

            for (KeyBinding keyBinding : keyBindings) {
                ID_TO_KEYBINDING_MAP.put(keyBinding.getTranslationKey(), keyBinding);
            }
            return getKeyBinding(name);

        }
    }

}
