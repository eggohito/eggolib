package io.github.eggohito.eggolib.component.entity;

import io.github.eggohito.eggolib.component.EggolibEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class GroupComponentImpl implements GroupComponent {

    private final Entity entity;
    private final Set<String> groups = new HashSet<>();

    public GroupComponentImpl(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void joinGroup(String name) {
        this.groups.add(name);
    }

    @Override
    public void leaveGroup(String name) {
        this.groups.remove(name);
    }

    @Override
    public void leaveAllGroups() {
        this.groups.clear();
    }

    @Override
    public void sync() {
        EggolibEntityComponents.GROUP.sync(entity);
    }

    @Override
    public Set<String> getGroups() {
        return groups;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.groups.clear();
        NbtList nbtList = tag.getList("Groups", NbtElement.STRING_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            this.groups.add(nbtList.getString(i));
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        NbtList nbtList = new NbtList();
        nbtList.addAll(groups.stream().map(NbtString::of).toList());
        tag.put("Groups", nbtList);
    }

}
