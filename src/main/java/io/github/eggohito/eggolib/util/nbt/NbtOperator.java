package io.github.eggohito.eggolib.util.nbt;

import com.google.common.collect.Iterables;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.List;

public enum NbtOperator {

	REPLACE((rootNbt, targetPath, sourceNbts) ->
		targetPath.put(rootNbt, Iterables.getLast(sourceNbts))),
	APPEND((rootNbt, targetPath, sourceNbts) -> targetPath.getOrInit(rootNbt, NbtList::new)
		.stream()
		.filter(foundNbt -> foundNbt instanceof NbtList)
		.map(foundNbt -> (NbtList) foundNbt)
		.forEach(foundNbt -> sourceNbts.forEach(sourceNbt -> foundNbt.add(sourceNbt.copy())))),
	MERGE((rootNbt, targetPath, sourceNbts) -> targetPath.getOrInit(rootNbt, NbtCompound::new)
		.stream()
		.filter(foundNbt -> foundNbt instanceof NbtCompound)
		.map(foundNbt -> (NbtCompound) foundNbt)
		.forEach(foundNbt -> sourceNbts
			.stream()
			.filter(sourceNbt -> sourceNbt instanceof NbtCompound)
			.map(sourceNbt -> (NbtCompound) sourceNbt)
			.forEach(foundNbt::copyFrom)));

	private final Applier applier;
	NbtOperator(Applier applier) {
		this.applier = applier;
	}

	public void apply(NbtElement rootNbt, NbtPathArgumentType.NbtPath targetPath, List<NbtElement> sourceNbts) throws CommandSyntaxException {
		this.applier.apply(rootNbt, targetPath, sourceNbts);
	}

	interface Applier {
		void apply(NbtElement rootNbt, NbtPathArgumentType.NbtPath targetPath, List<NbtElement> sourceNbts) throws CommandSyntaxException;
	}

}
