package io.github.eggohito.eggolib.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPlayerInteractionManager.class)
public interface ClientPlayerInteractionManagerAccessor {

    @Accessor("currentBreakingPos")
    BlockPos breakingBlockPos();

    @Accessor("breakingBlock")
    boolean breakingBlock();

}
