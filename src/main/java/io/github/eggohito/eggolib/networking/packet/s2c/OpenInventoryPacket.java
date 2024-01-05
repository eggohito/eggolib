package io.github.eggohito.eggolib.networking.packet.s2c;

import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.access.InventoryHolder;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;

public record OpenInventoryPacket(int entityId) implements FabricPacket {

	public static final PacketType<OpenInventoryPacket> TYPE = PacketType.create(
		Eggolib.identifier("s2c/open_inventory"), OpenInventoryPacket::read
	);

	private static OpenInventoryPacket read(PacketByteBuf buf) {
		return new OpenInventoryPacket(buf.readVarInt());
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeVarInt(entityId);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ClientPlayerEntity player, PacketSender responseSender) {

		Entity entity = player.getWorld().getEntityById(entityId);
		if (!(entity instanceof InventoryHolder inventoryHolder)) {
			Eggolib.LOGGER.warn("Received packet for opening the inventory of {}!", entity == null ? "unknown entity" : "entity without an inventory");
			return;
		}

		inventoryHolder.eggolib$openInventory();

	}

}
