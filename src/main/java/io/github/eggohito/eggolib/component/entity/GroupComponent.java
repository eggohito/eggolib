package io.github.eggohito.eggolib.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;

import java.util.Set;

public interface GroupComponent extends AutoSyncedComponent {
    void joinGroup(String name);
    void leaveGroup(String name);
    void leaveAllGroups();
    void sync();
    Set<String> getGroups();
}
