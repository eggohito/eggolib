package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.power.ActiveInteractionPower;
import io.github.eggohito.eggolib.power.ActionOnBlockPlacePower;
import io.github.eggohito.eggolib.power.PreventBlockPlacePower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "canPlace", at = @At("HEAD"), cancellable = true)
    private void eggolib$preventBlockPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos hitPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
        BlockPos placementPos = context.getBlockPos();
        Direction direction = context.getSide();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        int eggolib$preventBlockPlacePowers = 0;
        ActiveInteractionPower.CallInstance<PreventBlockPlacePower> pbppci = new ActiveInteractionPower.CallInstance<>();
        pbppci.add(playerEntity, PreventBlockPlacePower.class, pbpp -> pbpp.doesPrevent(hand, hitPos, placementPos, direction, itemStack));

        for (int i = pbppci.getMaxPriority(); i >= 0; i--) {

            if (!pbppci.hasPowers(i)) continue;

            List<PreventBlockPlacePower> pbpps = pbppci.getPowers(i);
            eggolib$preventBlockPlacePowers += pbpps.size();

            pbpps.forEach(pbpp -> pbpp.executeActions(hand, hitPos, placementPos, direction));

        }

        if (eggolib$preventBlockPlacePowers > 0) cir.setReturnValue(false);

    }

    @Unique private ActiveInteractionPower.CallInstance<ActionOnBlockPlacePower> eggolib$aobppci;

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void eggolib$actionOnBlockPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir, ItemPlacementContext itemPlacementContext, BlockState blockState, BlockPos blockPos, World world, PlayerEntity playerEntity, ItemStack itemStack) {

        BlockPos hitPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
        BlockPos placementPos = context.getBlockPos();
        Direction direction = context.getSide();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        eggolib$aobppci = new ActiveInteractionPower.CallInstance<>();
        eggolib$aobppci.add(playerEntity, ActionOnBlockPlacePower.class, aobpp -> aobpp.shouldExecute(hand, hitPos, placementPos, direction, itemStack));

        for (int i = eggolib$aobppci.getMaxPriority(); i >= 0; i--) {
            if (!eggolib$aobppci.hasPowers(i)) continue;
            eggolib$aobppci.getPowers(i).forEach(aobpp -> aobpp.executeBlockAndEntityActions(hitPos, placementPos, direction));
        }

    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("TAIL"))
    private void eggolib$postActionOnBlockPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        for (int i = eggolib$aobppci.getMaxPriority(); i >= 0; i--) {
            if (!eggolib$aobppci.hasPowers(i)) continue;
            eggolib$aobppci.getPowers(i).forEach(aobpp -> aobpp.executeItemActions(playerEntity, hand));
        }

    }

}
