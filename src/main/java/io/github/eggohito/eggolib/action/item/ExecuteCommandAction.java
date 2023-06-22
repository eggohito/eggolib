package io.github.eggohito.eggolib.action.item;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.command.DataCommandStorage;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

public class ExecuteCommandAction {

	public static void action(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {

		World world = worldAndStack.getLeft();
		ItemStack stack = worldAndStack.getRight();
		MinecraftServer server = world.getServer();

		if (server == null) {
			return;
		}

		Entity stackHolder = stack.getHolder();
		ServerCommandSource source = new ServerCommandSource(
			CommandOutput.DUMMY,
			stackHolder != null ? stackHolder.getPos() : world.getSpawnPos().toCenterPos(),
			stackHolder != null ? stackHolder.getRotationClient() : Vec2f.ZERO,
			(ServerWorld) world,
			Apoli.config.executeCommand.permissionLevel,
			"Server",
			Text.of("Server"),
			server,
			stackHolder
		);

		String command = data.get("command");
		Identifier storageId = data.get("storage_id");
		DataCommandStorage dataCommandStorage = server.getDataCommandStorage();

		if (storageId != null) {

			NbtCompound stackNbt = new NbtCompound();
			NbtCompound itemNbt = new NbtCompound();

			stackNbt.putString("id", Registries.ITEM.getId(stack.getItem()).toString());
			stackNbt.putByte("Count", (byte) stack.getCount());
			stackNbt.put("tag", stack.getOrCreateNbt());

			itemNbt.put("Item", stackNbt);

			NbtCompound storageNbt = dataCommandStorage.get(storageId);
			storageNbt.copyFrom(itemNbt);

			dataCommandStorage.set(storageId, storageNbt);

		}

		server.getCommandManager().executeWithPrefix(source, command);

	}

	public static ActionFactory<Pair<World, ItemStack>> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("execute_command"),
			new SerializableData()
				.add("command", SerializableDataTypes.STRING)
				.add("storage_id", SerializableDataTypes.IDENTIFIER, null),
			ExecuteCommandAction::action
		);
	}

}
