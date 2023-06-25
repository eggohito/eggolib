package io.github.eggohito.eggolib.action.item;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.command.DataCommandStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.function.CopyNbtLootFunction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
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
		List<CopyNbtLootFunction.Operation> operations = data.get("ops");
		DataCommandStorage commandStorage = server.getDataCommandStorage();

		NbtCompound stackNbt = new NbtCompound();
		NbtCompound itemNbt = new NbtCompound();

		stackNbt.putString("id", Registries.ITEM.getId(stack.getItem()).toString());
		stackNbt.putByte("Count", (byte) stack.getCount());
		stackNbt.put("tag", stack.getOrCreateNbt());

		itemNbt.put("Item", stackNbt);

		NbtCompound storageNbt = commandStorage.get(storageId);
		operations.forEach(op -> op.execute(() -> storageNbt, itemNbt));
		commandStorage.set(storageId, storageNbt);

	}

	public static ActionFactory<Pair<World, ItemStack>> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("copy_to_storage"),
			new SerializableData()
				.add("id", SerializableDataTypes.IDENTIFIER)
				.add("ops", EggolibDataTypes.NBT_OPERATIONS),
			CopyToStorageAction::action
		);
	}

}
