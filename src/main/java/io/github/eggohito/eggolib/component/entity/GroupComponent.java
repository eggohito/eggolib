package io.github.eggohito.eggolib.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.util.Identifier;

import java.util.Set;

public interface GroupComponent extends AutoSyncedComponent {
    void joinGroup(Identifier identifier);
    void leaveGroup(Identifier identifier);
    void leaveAllGroups();
    Set<Identifier> getGroups();
}
