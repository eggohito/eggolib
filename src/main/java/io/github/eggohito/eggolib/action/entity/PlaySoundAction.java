package io.github.eggohito.eggolib.action.entity;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;

public class PlaySoundAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        SoundEvent soundEvent = data.get("sound");
        SoundCategory soundCategory = data.get("category");

        float volume = data.get("volume");
        float pitch = data.get("pitch");

        entity.world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), soundEvent, soundCategory, volume, pitch);

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("play_sound"),
            new SerializableData()
                .add("sound", SerializableDataTypes.SOUND_EVENT)
                .add("category", EggolibDataTypes.SOUND_CATEGORY, SoundCategory.NEUTRAL)
                .add("volume", SerializableDataTypes.FLOAT, 1F)
                .add("pitch", SerializableDataTypes.FLOAT, 1F),
            PlaySoundAction::action
        );
    }

}
