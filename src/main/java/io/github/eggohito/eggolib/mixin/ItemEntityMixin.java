package io.github.eggohito.eggolib.mixin;

import io.github.eggohito.eggolib.power.ActionOnItemPickupPower;
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

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;sendPickup(Lnet/minecraft/entity/Entity;I)V"))
    private void eggolib$actionOnItemPickup(PlayerEntity playerEntity, CallbackInfo ci) {

        ItemEntity thisAsItemEntity = (ItemEntity) (Object) this;
        ItemStack itemStack = thisAsItemEntity.getStack();
        UUID ownerUUID = thisAsItemEntity.getOwner();

        Entity[] ownerEntity = {null};
        MinecraftServer minecraftServer = thisAsItemEntity.getServer();
        if (minecraftServer != null) {
            for (ServerWorld serverWorld : minecraftServer.getWorlds()) {
                if (ownerUUID != null) ownerEntity[0] = serverWorld.getEntity(ownerUUID);
                if (ownerEntity[0] != null) break;
            }
        }

        PrioritizedPower.SortedMap<ActionOnItemPickupPower> aoippsm = new PrioritizedPower.SortedMap<>();
        aoippsm.add(playerEntity, ActionOnItemPickupPower.class, aoipp -> aoipp.doesApply(itemStack, ownerEntity[0]));

        for (int i = aoippsm.getMaxPriority(); i >= 0; i--) {

            if (!aoippsm.hasPowers(i)) continue;

            List<ActionOnItemPickupPower> aoipps = aoippsm.getPowers(i);
            aoipps.forEach(aoipp -> aoipp.executeActions(itemStack, ownerEntity[0]));

        }

    }

}
