package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.power.ActionOnItemPickupPower;
import io.github.eggohito.eggolib.power.PreventItemPickupPower;
import io.github.eggohito.eggolib.power.PrioritizedPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
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
        UUID throwerUUID = ((ItemEntityAccessor) thisAsItemEntity).getThrower();

        //  Only trigger the execution process on the server-side
        MinecraftServer server = thisAsItemEntity.getServer();
        if (server == null) {
            return;
        }

        //  Get the thrower entity of this item entity
        Entity throwerEntity = null;
        if (throwerUUID != null) {
            for (ServerWorld serverWorld : thisAsItemEntity.getServer().getWorlds()) {
                if ((throwerEntity = serverWorld.getEntity(throwerUUID)) != null) {
                    break;
                }
            }
        }

        //  Copy the thrower entity to an effectively final variable
        Entity finalThrowerEntity = throwerEntity;

        //  Prevent the item entity from being picked up
        PrioritizedPower.CallInstance<PreventItemPickupPower> pippci = new PrioritizedPower.CallInstance<>();
        pippci.add(playerEntity, PreventItemPickupPower.class, pipp -> pipp.doesPrevent(itemStack, finalThrowerEntity));
        int preventItemPickupPowers = 0;

        for (int i = pippci.getMaxPriority(); i >= pippci.getMinPriority(); i--) {

            if (!pippci.hasPowers(i)) continue;
            List<PreventItemPickupPower> pipps = pippci.getPowers(i);

            preventItemPickupPowers += pipps.size();
            pipps.forEach(pipp -> pipp.executeActions(itemStack, finalThrowerEntity));

        }

        if (preventItemPickupPowers > 0) {
            ci.cancel();
            return;
        }

        //  Execute action(s) upon the item entity being picked up
        PrioritizedPower.CallInstance<ActionOnItemPickupPower> aoippci = new PrioritizedPower.CallInstance<>();
        aoippci.add(playerEntity, ActionOnItemPickupPower.class, aoipp -> aoipp.doesApply(itemStack, finalThrowerEntity));

        for (int i = aoippci.getMaxPriority(); i >= aoippci.getMinPriority(); i--) {

            if (!aoippci.hasPowers(i)) continue;

            List<ActionOnItemPickupPower> aoipps = aoippci.getPowers(i);
            aoipps.forEach(aoipp -> aoipp.executeActions(itemStack, finalThrowerEntity));

        }

    }

}
