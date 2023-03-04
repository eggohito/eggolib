package io.github.eggohito.eggolib.mixin;

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
import java.util.concurrent.atomic.AtomicReference;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    private void eggolib$onItemPickup(PlayerEntity playerEntity, CallbackInfo ci) {

        ItemEntity thisAsItemEntity = (ItemEntity) (Object) this;
        ItemStack itemStack = thisAsItemEntity.getStack();
        UUID throwerUUID = thisAsItemEntity.getThrower();

        //  Get the thrower entity of this item entity
        AtomicReference<Entity> atomicThrowerEntity = new AtomicReference<>();

        if (thisAsItemEntity.getServer() != null && throwerUUID != null) {
            for (ServerWorld serverWorld : thisAsItemEntity.getServer().getWorlds()) {
                atomicThrowerEntity.set(serverWorld.getEntity(throwerUUID));
                if (atomicThrowerEntity.get() != null) break;
            }
        }

        //  Prevent the item entity from being picked up
        PrioritizedPower.CallInstance<PreventItemPickupPower> pippci = new PrioritizedPower.CallInstance<>();
        pippci.add(playerEntity, PreventItemPickupPower.class, pipp -> pipp.doesPrevent(itemStack, atomicThrowerEntity.get()));
        int preventItemPickupPowers = 0;

        for (int i = pippci.getMaxPriority(); i >= pippci.getMinPriority(); i--) {

            if (!pippci.hasPowers(i)) continue;
            List<PreventItemPickupPower> pipps = pippci.getPowers(i);

            preventItemPickupPowers += pipps.size();
            pipps.forEach(pipp -> pipp.executeActions(itemStack, atomicThrowerEntity.get()));

        }

        if (preventItemPickupPowers > 0) {
            ci.cancel();
            return;
        }

        //  Execute an action upon the item entity being picked up
        PrioritizedPower.CallInstance<ActionOnItemPickupPower> aoippci = new PrioritizedPower.CallInstance<>();
        aoippci.add(playerEntity, ActionOnItemPickupPower.class, aoipp -> aoipp.doesApply(itemStack, atomicThrowerEntity.get()));

        for (int i = aoippci.getMaxPriority(); i >= aoippci.getMinPriority(); i--) {

            if (!aoippci.hasPowers(i)) continue;

            List<ActionOnItemPickupPower> aoipps = aoippci.getPowers(i);
            aoipps.forEach(aoipp -> aoipp.executeActions(itemStack, atomicThrowerEntity.get()));

        }

    }

}
