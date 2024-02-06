package io.github.eggohito.eggolib.util.nbt;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.nbt.NbtElement;

import java.util.List;
import java.util.function.Supplier;

public record NbtOperation(NbtPathArgumentType.NbtPath parsedSourcePath, NbtPathArgumentType.NbtPath parsedTargetPath, NbtOperator operator) {

	public void execute(Supplier<NbtElement> rootNbtSupplier, NbtElement sourceNbt) {
		try {

			List<NbtElement> sourceNbts = parsedSourcePath.get(sourceNbt);

			if (!sourceNbts.isEmpty()) {
				operator.apply(rootNbtSupplier.get(), parsedTargetPath, sourceNbts);
			}

		} catch (CommandSyntaxException ignored) {
			//  No op!
		}
	}

}
