package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.power.ActiveInteractionPower;
import io.github.eggohito.eggolib.power.EggolibPreventItemUsePower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void eggolib$preventItemUse(World world, PlayerEntity playerEntity, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {

        if (playerEntity == null) return;

        int eggolib$preventItemUsePowers = 0;
        ItemStack stackInHand = playerEntity.getStackInHand(hand);
        ActiveInteractionPower.CallInstance<EggolibPreventItemUsePower> epiupci = new ActiveInteractionPower.CallInstance<>();
        epiupci.add(playerEntity, EggolibPreventItemUsePower.class, epiup -> epiup.shouldExecute(hand, stackInHand));

        for (int i = epiupci.getMaxPriority(); i >= 0; i--) {

            if (!epiupci.hasPowers(i)) continue;

            List<EggolibPreventItemUsePower> epiups = epiupci.getPowers(i);
            eggolib$preventItemUsePowers += epiups.size();

            epiups.forEach(epiup -> epiup.executeActions(hand));

        }

        if (eggolib$preventItemUsePowers > 0) cir.setReturnValue(TypedActionResult.fail(stackInHand));


    }

}
