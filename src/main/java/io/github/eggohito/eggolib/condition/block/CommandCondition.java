package io.github.eggohito.eggolib.condition.block;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;

public class CommandCondition {

	public static boolean condition(SerializableData.Instance data, CachedBlockPosition cachedBlockPosition) {

		MinecraftServer server = ((World) cachedBlockPosition.getWorld()).getServer();
		if (server == null) {
			return false;
		}

		Comparison comparison = data.get("comparison");
		String command = data.get("command");

		ServerCommandSource source = new ServerCommandSource(
			CommandOutput.DUMMY,
			cachedBlockPosition.getBlockPos().toCenterPos(),
			Vec2f.ZERO,
			(ServerWorld) cachedBlockPosition.getWorld(),
			Apoli.config.executeCommand.permissionLevel,
			"Server",
			Text.of("Server"),
			server,
			null
		);

		int compareTo = data.get("compare_to");
		int result = server.getCommandManager().executeWithPrefix(source, command);

		return comparison.compare(result, compareTo);

	}

	public static ConditionFactory<CachedBlockPosition> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("command"),
			new SerializableData()
				.add("command", SerializableDataTypes.STRING)
				.add("comparison", ApoliDataTypes.COMPARISON, Comparison.GREATER_THAN)
				.add("compare_to", SerializableDataTypes.INT, 0),
			CommandCondition::condition
		);
	}

}
