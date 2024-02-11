package io.github.eggohito.eggolib.component.entity;

import io.github.eggohito.eggolib.component.EggolibComponents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.HashSet;
import java.util.Set;

public class MiscComponent implements IMiscComponent {

	private final Set<String> syncedCommandTags = new HashSet<>();
	private final Entity entity;

	private boolean dirty;

	public MiscComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Set<String> getCommandTags() {
		return syncedCommandTags;
	}

	@Override
	public void setCommandTags(Set<String> commandTags) {

		if (this.syncedCommandTags.equals(commandTags)) {
			return;
		}

		this.syncedCommandTags.clear();
		this.syncedCommandTags.addAll(commandTags);

		this.dirty = true;

	}

	@Override
	public boolean removeCommandTag(String commandTag) {
		return this.syncedCommandTags.remove(commandTag)
			&& (this.dirty = true);
	}

	@Override
	public boolean addCommandTag(String commandTag) {
		return this.syncedCommandTags.add(commandTag)
			&& (this.dirty = true);
	}

	@Override
	public void sync(boolean force) {
		if (force || dirty) {
			this.dirty = false;
			this.sync();
		}
	}

	@Override
	public void sync() {
		EggolibComponents.MISC.sync(entity);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {

		NbtList commandTagsNbt = tag.getList("Tags", NbtElement.STRING_TYPE);
		this.syncedCommandTags.clear();

		for (int i = 0; i < commandTagsNbt.size(); i++) {
			this.syncedCommandTags.add(commandTagsNbt.getString(i));
		}

	}

	@Override
	public void writeToNbt(NbtCompound tag) {

		NbtList commandTagsNbt = new NbtList();
		this.entity.commandTags
			.stream()
			.map(NbtString::of)
			.forEach(commandTagsNbt::add);

		tag.put("Tags", commandTagsNbt);

	}

}
