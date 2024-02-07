package io.github.eggohito.eggolib.util.nbt;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.calio.util.ArgumentWrapper;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtElement;

import java.util.List;
import java.util.function.Supplier;

public record NbtOperation(ArgumentWrapper<NbtPathArgumentType.NbtPath> wrappedSourcePath, ArgumentWrapper<NbtPathArgumentType.NbtPath> wrappedTargetPath, NbtOperator operator) {

	public void execute(Supplier<NbtElement> rootNbtSupplier, NbtElement sourceNbt) {
		try {

			List<NbtElement> sourceNbts = wrappedSourcePath.get().get(sourceNbt);

			if (!sourceNbts.isEmpty()) {
				operator.apply(rootNbtSupplier.get(), wrappedTargetPath.get(), sourceNbts);
			}

		} catch (CommandSyntaxException ignored) {
			//  No op!
		}
	}

}
