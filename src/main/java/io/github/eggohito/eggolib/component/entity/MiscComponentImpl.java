package io.github.eggohito.eggolib.component.entity;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.HashSet;
import java.util.Set;

public class MiscComponentImpl implements MiscComponent {

    private final Entity entity;
    private Set<String> scoreboardTags = new HashSet<>();

    public MiscComponentImpl(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Set<String> getScoreboardTags() {
        return scoreboardTags;
    }

    @Override
    public void copyScoreboardTagsFrom(Set<String> tags) {
        this.scoreboardTags = tags;
        sync();
    }

    @Override
    public void removeScoreboardTag(String tag) {
        this.scoreboardTags.remove(tag);
        sync();
    }

    @Override
    public void addScoreboardTag(String tag) {
        this.scoreboardTags.add(tag);
        sync();
    }

    @Override
    public void sync() {
        MiscComponent.KEY.sync(entity);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.scoreboardTags.clear();
        NbtList nbtList = tag.getList("Tags", NbtElement.STRING_TYPE);
        for (int i = 0; i < nbtList.size(); i++) {
            this.scoreboardTags.add(nbtList.getString(i));
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        NbtList nbtList = new NbtList();
        nbtList.addAll(this.scoreboardTags.stream().map(NbtString::of).toList());
        tag.put("Tags", nbtList);
    }

}
