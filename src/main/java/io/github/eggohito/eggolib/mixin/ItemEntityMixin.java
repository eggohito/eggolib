package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.ActionOnItemPickupPower;
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

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"))
    private void eggolib$actionOnItemPickup(PlayerEntity playerEntity, CallbackInfo ci) {

        ItemEntity thisAsItemEntity = (ItemEntity) (Object) this;
        ItemStack itemStack = thisAsItemEntity.getStack();
        UUID throwerUUID = thisAsItemEntity.getThrower();

        Entity[] throwerEntity = {null};
        if (Eggolib.minecraftServer != null) {
            for (ServerWorld serverWorld : Eggolib.minecraftServer.getWorlds()) {
                if (throwerUUID != null) throwerEntity[0] = serverWorld.getEntity(throwerUUID);
                if (throwerEntity[0] != null) break;
            }
        }

        PrioritizedPower.SortedMap<ActionOnItemPickupPower> aoippsm = new PrioritizedPower.SortedMap<>();
        aoippsm.add(playerEntity, ActionOnItemPickupPower.class, aoipp -> aoipp.doesApply(itemStack, throwerEntity[0]));

        for (int i = aoippsm.getMaxPriority(); i >= 0; i--) {

            if (!aoippsm.hasPowers(i)) continue;

            List<ActionOnItemPickupPower> aoipps = aoippsm.getPowers(i);
            aoipps.forEach(aoipp -> aoipp.executeActions(itemStack, throwerEntity[0]));

        }

    }

}
