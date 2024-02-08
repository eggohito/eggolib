package io.github.eggohito.eggolib.mixin;

import com.mojang.authlib.GameProfile;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.access.InventoryHolder;
import io.github.eggohito.eggolib.networking.packet.s2c.OpenInventoryS2CPacket;
import io.github.eggohito.eggolib.power.StatPower;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements InventoryHolder {

	public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
		super(world, pos, yaw, gameProfile);
	}

	@Override
	public void eggolib$openInventory() {
		if (!(this.currentScreenHandler instanceof PlayerScreenHandler)) {
			ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, new OpenInventoryS2CPacket(this.getId()));
		}
	}

	@Inject(method = "increaseStat", at = @At("TAIL"))
	private void eggolib$increaseStatValue(Stat<?> stat, int amount, CallbackInfo ci) {
		PowerHolderComponent.getPowers(this, StatPower.class).forEach(sp -> sp.increaseValue(stat, amount));
	}

}
