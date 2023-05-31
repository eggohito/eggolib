package io.github.eggohito.eggolib.mixin;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerInteractionManager.class)
public interface ServerPlayerInteractionManagerAccessor {

	@Accessor("miningPos")
	BlockPos breakingBlockPos();

	@Accessor("mining")
	boolean breakingBlock();

}
