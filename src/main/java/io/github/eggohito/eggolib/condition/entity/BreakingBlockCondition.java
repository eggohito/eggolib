package io.github.eggohito.eggolib.condition.entity;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.condition.ConditionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.ClientPlayerEntityAccessor;
import io.github.eggohito.eggolib.mixin.ClientPlayerInteractionManagerAccessor;
import io.github.eggohito.eggolib.mixin.ServerPlayerInteractionManagerAccessor;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.function.Predicate;

public class BreakingBlockCondition {

	public static boolean condition(SerializableData.Instance data, Entity entity) {

		if (!(entity instanceof PlayerEntity playerEntity)) {
			return false;
		}

		Predicate<CachedBlockPosition> blockCondition = data.get("block_condition");
		boolean usingEffectiveTool = data.getBoolean("using_effective_tool");
		BlockPos breakingBlockPos;

		if (playerEntity.getWorld().isClient) {
			ClientPlayerInteractionManagerAccessor cpim = (ClientPlayerInteractionManagerAccessor) ((ClientPlayerEntityAccessor) playerEntity).getClient().interactionManager;
			breakingBlockPos = cpim != null && cpim.breakingBlock() ? cpim.breakingBlockPos() : null;
		} else {
			ServerPlayerInteractionManagerAccessor spim = (ServerPlayerInteractionManagerAccessor) ((ServerPlayerEntity) playerEntity).interactionManager;
			breakingBlockPos = spim.breakingBlock() ? spim.breakingBlockPos() : null;
		}

		if (breakingBlockPos == null) {
			return false;
		}

		if (usingEffectiveTool) {
			return playerEntity.canHarvest(playerEntity.getWorld().getBlockState(breakingBlockPos)) && (blockCondition == null || blockCondition.test(new CachedBlockPosition(entity.getWorld(), breakingBlockPos, true)));
		} else {
			return blockCondition == null || blockCondition.test(new CachedBlockPosition(entity.getWorld(), breakingBlockPos, true));
		}

	}

	public static ConditionFactory<Entity> getFactory() {
		return new ConditionFactory<>(
			Eggolib.identifier("breaking_block"),
			new SerializableData()
				.add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
				.add("using_effective_tool", SerializableDataTypes.BOOLEAN, false),
			BreakingBlockCondition::condition
		);
	}

}
