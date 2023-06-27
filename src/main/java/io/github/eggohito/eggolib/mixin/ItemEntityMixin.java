package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.power.Prioritized;
import io.github.eggohito.eggolib.power.ActionOnItemPickupPower;
import io.github.eggohito.eggolib.power.PreventItemPickupPower;
import io.github.eggohito.eggolib.power.PrioritizedPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity implements Ownable {

	@Shadow
	@Nullable
	private UUID thrower;

	@Shadow
	public abstract ItemStack getStack();

	public ItemEntityMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
	private void eggolib$onItemPickup(PlayerEntity playerEntity, CallbackInfo ci) {

		ItemStack itemStack = this.getStack();
		UUID throwerUUID = this.thrower;

		//  Only trigger the execution process on the server-side
		MinecraftServer server = this.getServer();
		if (server == null) {
			return;
		}

		//  Get the thrower entity of this item entity
		Entity throwerEntity = null;
		if (throwerUUID != null) {
			for (ServerWorld serverWorld : this.getServer().getWorlds()) {
				if ((throwerEntity = serverWorld.getEntity(throwerUUID)) != null) {
					break;
				}
			}
		}

		//  Copy the thrower entity to an effectively final variable
		Entity finalThrowerEntity = throwerEntity;

		//  Prevent the item entity from being picked up
		Prioritized.CallInstance<PrioritizedPower> pippci = new Prioritized.CallInstance<>();
		pippci.add(playerEntity, PreventItemPickupPower.class, pipp -> pipp.doesPrevent(itemStack, finalThrowerEntity));
		int preventItemPickupPowers = 0;

		for (int i = pippci.getMaxPriority(); i >= pippci.getMinPriority(); i--) {

			if (!pippci.hasPowers(i)) {
				continue;
			}

			List<PreventItemPickupPower> pipps = pippci.getPowers(i)
				.stream()
				.filter(p -> p instanceof PreventItemPickupPower)
				.map(p -> (PreventItemPickupPower) p)
				.toList();

			preventItemPickupPowers += pipps.size();
			pipps.forEach(pipp -> pipp.executeActions(itemStack, finalThrowerEntity));

		}

		if (preventItemPickupPowers > 0) {
			ci.cancel();
			return;
		}

		//  Execute action(s) upon the item entity being picked up
		Prioritized.CallInstance<PrioritizedPower> aoippci = new Prioritized.CallInstance<>();
		aoippci.add(playerEntity, ActionOnItemPickupPower.class, aoipp -> aoipp.doesApply(itemStack, finalThrowerEntity));

		for (int i = aoippci.getMaxPriority(); i >= aoippci.getMinPriority(); i--) {

			if (!aoippci.hasPowers(i)) {
				continue;
			}

			aoippci.getPowers(i)
				.stream()
				.filter(p -> p instanceof ActionOnItemPickupPower)
				.map(p -> (ActionOnItemPickupPower) p)
				.forEach(p -> p.executeActions(itemStack, finalThrowerEntity));

		}

	}

}
