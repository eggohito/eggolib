package io.github.eggohito.eggolib.action.item;

import io.github.apace100.apoli.access.MutableItemStack;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.loot.context.EggolibLootContextTypes;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ModifyAction {

	public static void action(SerializableData.Instance data, Pair<World, ItemStack> worldAndStack) {

		MinecraftServer server = worldAndStack.getLeft().getServer();
		if (server == null) {
			return;
		}

		Identifier itemModifierId = data.get("modifier");
		LootFunction itemModifier = server.getItemModifierManager().get(itemModifierId);
		if (itemModifier == null) {
			Eggolib.LOGGER.error("Unknown item modifier (\"" + itemModifierId + "\") used in `modify` item action type!");
			return;
		}

		ServerWorld world = server.getWorld(data.get("dimension"));
		if (world == null) {
			world = server.getOverworld();
		}

		Vec3d pos = data.isPresent("position") ? data.get("position") : world.getSpawnPos().toCenterPos();
		BlockPos blockPos = BlockPos.ofFloored(pos);
		ItemStack stack = worldAndStack.getRight();
		Entity stackHolder = stack.getHolder();
		LootContext lootContext = new LootContext.Builder(world)
			.parameter(LootContextParameters.ORIGIN, pos)
			.parameter(LootContextParameters.TOOL, stack)
			.optionalParameter(LootContextParameters.THIS_ENTITY, stackHolder)
			.optionalParameter(LootContextParameters.BLOCK_STATE, world.getBlockState(blockPos))
			.optionalParameter(LootContextParameters.BLOCK_ENTITY, world.getBlockEntity(blockPos))
			.build(EggolibLootContextTypes.ANY);

		ItemStack newStack = itemModifier.apply(stack, lootContext);
		((MutableItemStack) stack).setFrom(newStack);

	}

	public static ActionFactory<Pair<World, ItemStack>> getFactory() {
		return new ActionFactory<>(
			Eggolib.identifier("modify"),
			new SerializableData()
				.add("modifier", SerializableDataTypes.IDENTIFIER)
				.add("position", SerializableDataTypes.VECTOR, null)
				.add("dimension", SerializableDataTypes.DIMENSION, World.OVERWORLD),
			ModifyAction::action
		);
	}

}
