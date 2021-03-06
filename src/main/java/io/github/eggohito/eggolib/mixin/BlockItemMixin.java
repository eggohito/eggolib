package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.power.ActiveInteractionPower;
import io.github.eggohito.eggolib.power.ActionOnBlockPlacePower;
import io.github.eggohito.eggolib.power.EggolibPreventItemUsePower;
import io.github.eggohito.eggolib.power.PreventBlockPlacePower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
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

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;"), cancellable = true)
    private void eggolib$preventBlockItemUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        int eggolib$preventItemUsePowers = 0;
        ActiveInteractionPower.CallInstance<EggolibPreventItemUsePower> epiupci = new ActiveInteractionPower.CallInstance<>();
        epiupci.add(playerEntity, EggolibPreventItemUsePower.class, epiup -> epiup.shouldExecute(hand, itemStack));

        for (int i = epiupci.getMaxPriority(); i >= 0;  i--) {

            if (!epiupci.hasPowers(i)) continue;

            List<EggolibPreventItemUsePower> epiups = epiupci.getPowers(i);
            eggolib$preventItemUsePowers += epiups.size();

            epiups.forEach(epiup -> epiup.executeActions(hand));

        }

        if (eggolib$preventItemUsePowers > 0) cir.setReturnValue(ActionResult.FAIL);

    }

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

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"))
    private void eggolib$actionOnBlockPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();
        BlockPos hitPos = ((ItemUsageContextAccessor) context).callGetHitResult().getBlockPos();
        BlockPos placementPos = context.getBlockPos();
        Direction direction = context.getSide();
        Hand hand = context.getHand();

        if (playerEntity == null) return;

        ActiveInteractionPower.CallInstance<ActionOnBlockPlacePower> aobppci = new ActiveInteractionPower.CallInstance<>();
        aobppci.add(playerEntity, ActionOnBlockPlacePower.class, aobpp -> aobpp.shouldExecute(hand, hitPos, placementPos, direction, itemStack));

        for (int i = aobppci.getMaxPriority(); i >= 0; i--) {

            if (!aobppci.hasPowers(i)) continue;

            List<ActionOnBlockPlacePower> aobpps = aobppci.getPowers(i);
            aobpps.forEach(aobpp -> aobpp.executeActions(hand, hitPos, placementPos, direction));

        }

    }

}
