package io.github.eggohito.eggolib.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.Set;

public interface MiscComponent extends AutoSyncedComponent {
    Set<String> getScoreboardTags();
    void copyScoreboardTagsFrom(Set<String> tags);
    void removeScoreboardTag(String tag);
    void addScoreboardTag(String tag);
    void sync();
}
