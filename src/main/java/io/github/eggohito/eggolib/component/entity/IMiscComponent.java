package io.github.eggohito.eggolib.component.entity;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.Set;

@SuppressWarnings("UnusedReturnValue")
public interface IMiscComponent extends AutoSyncedComponent {

	Set<String> getCommandTags();

	void setCommandTags(Set<String> commandTags);

	boolean removeCommandTag(String commandTag);

	boolean addCommandTag(String commandTag);

	void sync(boolean force);

	void sync();

}
