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

	private final Set<String> commandTags = new HashSet<>();
	private final Entity entity;

	public MiscComponent(Entity entity) {
		this.entity = entity;
	}

	@Override
	public Set<String> getCommandTags() {
		return commandTags;
	}

	@Override
	public boolean removeCommandTag(String commandTag) {
		return this.commandTags.remove(commandTag);
	}

	@Override
	public boolean addCommandTag(String commandTag) {
		return this.commandTags.add(commandTag);
	}

	@Override
	public void sync() {
		EggolibComponents.MISC.sync(entity);
	}

	@Override
	public void readFromNbt(NbtCompound tag) {

		NbtList commandTagsNbt = tag.getList("Tags", NbtElement.STRING_TYPE);
		this.commandTags.clear();

		for (int i = 0; i < commandTagsNbt.size(); i++) {
			this.commandTags.add(commandTagsNbt.getString(i));
		}

	}

	@Override
	public void writeToNbt(NbtCompound tag) {

		NbtList commandTagsNbt = new NbtList();
		this.commandTags
			.stream()
			.map(NbtString::of)
			.forEach(commandTagsNbt::add);

		tag.put("Tags", commandTagsNbt);

	}

}
