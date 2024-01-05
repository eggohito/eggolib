package io.github.eggohito.eggolib.mixin;

import com.mojang.authlib.GameProfile;
import io.github.eggohito.eggolib.access.InventoryHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements InventoryHolder {

	@Shadow
	@Final
	protected MinecraftClient client;

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	public void eggolib$openInventory() {
		if (!(this.currentScreenHandler instanceof PlayerScreenHandler)) {
			this.client.getTutorialManager().onInventoryOpened();
			this.client.setScreen(new InventoryScreen(this));
		}
	}

}
