package io.github.eggohito.eggolib.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.Set;

public interface IMiscComponent extends AutoSyncedComponent {

	Set<String> getCommandTags();

	boolean removeCommandTag(String commandTag);

	boolean addCommandTag(String commandTag);

	void sync();

}
