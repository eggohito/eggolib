package io.github.eggohito.eggolib.networking.packet.c2s;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.power.PreventKeyUsePower;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;

public record SyncPreventedKeyPacket(String key, List<Identifier> powerIds) implements FabricPacket {

	public static final PacketType<SyncPreventedKeyPacket> TYPE = PacketType.create(
		Eggolib.identifier("sync_prevented_key"), SyncPreventedKeyPacket::create
	);

	private static SyncPreventedKeyPacket create(PacketByteBuf buf) {

		String key = buf.readString();
		List<Identifier> powerIds = new LinkedList<>();
		
		int powerIdsSize = buf.readInt();
		for (int i = 0; i < powerIdsSize; i++) {
			powerIds.add(buf.readIdentifier());
		}

		return new SyncPreventedKeyPacket(key, powerIds);

	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeString(key);
		buf.writeInt(powerIds.size());
		powerIds.forEach(buf::writeIdentifier);
	}

	@Override
	public PacketType<?> getType() {
		return TYPE;
	}

	public void handle(ServerPlayerEntity player, PacketSender responseSender) {

		PowerHolderComponent component = PowerHolderComponent.KEY.get(player);
		for (Identifier powerId : powerIds) {

			PowerType<?> powerType = PowerTypeRegistry.get(powerId);
			if (powerType == null) {
				continue;
			}

			Power power = component.getPower(powerType);
			if (power instanceof PreventKeyUsePower pkup) {
				pkup.executeActions(key);
			}

		}

	}

}
