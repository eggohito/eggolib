package io.github.eggohito.eggolib.component.entity;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import io.github.eggohito.eggolib.Eggolib;

import java.util.Set;

public interface MiscComponent extends AutoSyncedComponent {

    ComponentKey<MiscComponent> KEY = ComponentRegistry.getOrCreate(Eggolib.identifier("misc"), MiscComponent.class);

    Set<String> getScoreboardTags();

    void copyScoreboardTagsFrom(Set<String> tags);

    void removeScoreboardTag(String tag);

    void addScoreboardTag(String tag);

    void sync();

}
