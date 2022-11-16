package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ActionOnItemPickupPower;
import io.github.eggohito.eggolib.power.PreventItemPickupPower;
import io.github.eggohito.eggolib.power.PrioritizedPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    private void eggolib$actionOnItemPickup(PlayerEntity playerEntity, CallbackInfo ci) {

        ItemEntity thisAsItemEntity = (ItemEntity) (Object) this;
        ItemStack itemStack = thisAsItemEntity.getStack();
        UUID throwerUUID = thisAsItemEntity.getThrower();

        //  Get the thrower entity of this item entity
        Entity[] singletonThrowerEntity = {null};

        if (Eggolib.minecraftServer != null && throwerUUID != null) {
            for (ServerWorld serverWorld : Eggolib.minecraftServer.getWorlds()) {
                singletonThrowerEntity[0] = serverWorld.getEntity(throwerUUID);
                if (singletonThrowerEntity[0] != null) break;
            }
        }

        //  PreventItemPickupPower
        PrioritizedPower.SortedMap<PreventItemPickupPower> pippsm = new PrioritizedPower.SortedMap<>();
        pippsm.add(playerEntity, PreventItemPickupPower.class, pipp -> pipp.doesPrevent(itemStack, singletonThrowerEntity[0]));
        int j = 0;

        for (int i = pippsm.getMaxPriority(); i >= 0; i--) {

            if (!pippsm.hasPowers(i)) continue;

            List<PreventItemPickupPower> pipps = pippsm.getPowers(i);
            pipps.forEach(pipp -> pipp.executeActions(itemStack, singletonThrowerEntity[0]));
            j++;

        }

        if (j > 0) {
            ci.cancel();
            return;
        }

        //  ActionOnItemPickupPower
        PrioritizedPower.SortedMap<ActionOnItemPickupPower> aoippsm = new PrioritizedPower.SortedMap<>();
        aoippsm.add(playerEntity, ActionOnItemPickupPower.class, aoipp -> aoipp.doesApply(itemStack, singletonThrowerEntity[0]));

        for (int i = aoippsm.getMaxPriority(); i >= 0; i--) {

            if (!aoippsm.hasPowers(i)) continue;

            List<ActionOnItemPickupPower> aoipps = aoippsm.getPowers(i);
            aoipps.forEach(aoipp -> aoipp.executeActions(itemStack, singletonThrowerEntity[0]));

        }

    }

}
