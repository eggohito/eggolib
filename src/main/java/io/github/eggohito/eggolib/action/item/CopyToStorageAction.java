package io.github.eggohito.eggolib.action.item;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.power.factory.action.item.ItemActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import io.github.eggohito.eggolib.util.nbt.NbtOperation;
import net.minecraft.command.DataCommandStorage;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

import java.util.List;

public class CopyToStorageAction {

	public static void action(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {

		World world = worldAndStack.getLeft();
		ItemStack stack = worldAndStack.getRight();
		MinecraftServer server = world.getServer();

		if (server == null) {
			return;
		}

		Identifier storageId = data.get("id");
		List<NbtOperation> operations = data.get("ops");
		DataCommandStorage commandStorage = server.getDataCommandStorage();

		NbtCompound stackNbt = ItemStack.CODEC.encodeStart(NbtOps.INSTANCE, stack)
			.result()
			.filter(nbt -> nbt instanceof NbtCompound)
			.map(nbt -> (NbtCompound) nbt)
			.orElse(new NbtCompound());

		NbtCompound itemNbt = new NbtCompound();
		itemNbt.put("Item", stackNbt);

		NbtCompound commandStorageNbt = commandStorage.get(storageId);
		operations.forEach(op -> op.execute(() -> commandStorageNbt, itemNbt));

		commandStorage.set(storageId, commandStorageNbt);

	}

	public static ActionFactory<Pair<World, StackReference>> getFactory() {
		return ItemActionFactory.createItemStackBased(
			Eggolib.identifier("copy_to_storage"),
			new SerializableData()
				.add("id", SerializableDataTypes.IDENTIFIER)
				.add("ops", EggolibDataTypes.NBT_OPERATIONS),
			CopyToStorageAction::action
		);
	}

}
