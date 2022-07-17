package io.github.eggohito.eggolib.component.entity;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class GroupComponentImpl implements GroupComponent {

    private final Set<Identifier> groupIds = new HashSet<>();

    @Override
    public void joinGroup(Identifier groupId) {
        this.groupIds.add(groupId);
    }

    @Override
    public void leaveGroup(Identifier groupId) {
        this.groupIds.remove(groupId);
    }

    @Override
    public void leaveAllGroups() {
        this.groupIds.clear();
    }

    @Override
    public Set<Identifier> getGroups() {
        return groupIds;
    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        this.groupIds.clear();
        NbtList nbtList = tag.getList("ids", NbtElement.STRING_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            this.groupIds.add(new Identifier(nbtList.getString(i)));
        }
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        NbtList nbtList = new NbtList();
        nbtList.addAll(groupIds.stream().map(Identifier::toString).map(NbtString::of).toList());
        tag.put("ids", nbtList);
    }

}
