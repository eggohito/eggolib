package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.PreventBlockPlacePower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "canPlace", at = @At("HEAD"), cancellable = true)
    private void eggolib$preventBlockPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        Hand hand = context.getHand();

        if (playerEntity != null) {
            PowerHolderComponent phc = PowerHolderComponent.KEY.get(playerEntity);
            for (PreventBlockPlacePower pbpp : phc.getPowers(PreventBlockPlacePower.class)) {
                if (pbpp.doesApply(blockPos, direction, itemStack, hand)) cir.setReturnValue(pbpp.executeAction(hand));
            }
        }
    }

}
