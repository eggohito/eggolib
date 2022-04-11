package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.EggolibPreventItemUsePower;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/BlockItem;use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;"), cancellable = true)
    private void eggolib$preventBlockItemUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (playerEntity != null) {
            PowerHolderComponent phc = PowerHolderComponent.KEY.get(playerEntity);
            if (phc.getPowers(EggolibPreventItemUsePower.class).stream().anyMatch(elpiup -> elpiup.doesPrevent(itemStack))) {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }

    @Inject(method = "canPlace", at = @At("HEAD"), cancellable = true)
    private void eggolib$preventBlockItemPlace(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {

        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (playerEntity != null) {
            PowerHolderComponent phc = PowerHolderComponent.KEY.get(playerEntity);
            if (phc.getPowers(EggolibPreventItemUsePower.class).stream().anyMatch(elpiup -> elpiup.doesPreventPlacement(itemStack))) {
                cir.setReturnValue(false);
            }
        }
    }
}
