package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.util.SavedBlockPosition;
import io.github.eggohito.eggolib.power.ModifyBouncinessPower;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Unique private boolean eggolib$evalCachedBlock = false;
    @Unique private SavedBlockPosition eggolib$cachedBlock;

    //  TODO: Make it work reliably for blocks that turn to other blocks, like Farmland
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void eggolib$preOnLand(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {

        eggolib$evalCachedBlock = true;
        eggolib$cachedBlock = new SavedBlockPosition(world, pos);

        List<ModifyBouncinessPower> mbps =  PowerHolderComponent.getPowers(entity, ModifyBouncinessPower.class)
            .stream()
            .filter(mbp -> mbp.doesApply(eggolib$cachedBlock))
            .toList();
        if (mbps.isEmpty()) return;

        mbps.forEach(mbp -> mbp.executeActions(world, pos));
        ci.cancel();

    }

    @Inject(method = "onEntityLand", at = @At("HEAD"), cancellable = true)
    private void eggolib$modifyBounciness(BlockView world, Entity entity, CallbackInfo ci) {

    	if (eggolib$cachedBlock == null || !eggolib$evalCachedBlock) return;
        if (!PowerHolderComponent.hasPower(entity, ModifyBouncinessPower.class, mbp -> mbp.doesApply(eggolib$cachedBlock))) return;

        double yMultiplier = PowerHolderComponent.modify(entity, ModifyBouncinessPower.class, 0.8, mbp -> mbp.doesApply(eggolib$cachedBlock), null);
        entity.setVelocity(entity.getVelocity().multiply(1.0, -yMultiplier, 1.0));
        eggolib$evalCachedBlock = false;
        ci.cancel();

    }

}
