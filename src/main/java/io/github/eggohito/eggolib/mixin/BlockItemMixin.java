package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "canPlace", at = @At("HEAD"), cancellable = true)
    private void eggolib$preventBlockPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos hitPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
        Direction direction = context.getSide();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        List<PreventBlockPlacePower> filteredPreventBlockPlacePowers = PowerHolderComponent.KEY
            .get(playerEntity)
            .getPowers(PreventBlockPlacePower.class)
            .stream()
            .filter(preventBlockPlacePower -> preventBlockPlacePower.doesPrevent(hitPos, direction, itemStack, hand))
            .toList();

        if (filteredPreventBlockPlacePowers.size() > 0) {
            filteredPreventBlockPlacePowers.forEach(preventBlockPlacePower -> preventBlockPlacePower.executeActions(hand, hitPos, direction));
            cir.setReturnValue(false);
        }

    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/BlockItem;postPlacement(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/block/BlockState;)Z"), cancellable = true)
    private void eggolib$actionOnBlockPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos hitPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
        BlockPos placementPos = context.getBlockPos();
        Direction direction = context.getSide();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        List<ActionOnBlockPlacePower> filteredActionOnBlockPlacePowers = PowerHolderComponent.KEY
            .get(playerEntity)
            .getPowers(ActionOnBlockPlacePower.class)
            .stream()
            .filter(actionOnBlockPlacePower -> actionOnBlockPlacePower.shouldExecute(hand, hitPos, direction, itemStack))
            .toList();

        if (filteredActionOnBlockPlacePowers.size() > 0) {
            filteredActionOnBlockPlacePowers.forEach(
                actionOnBlockPlacePower -> actionOnBlockPlacePower.executeActions(hand, placementPos, direction)
            );
        }

    }

}
