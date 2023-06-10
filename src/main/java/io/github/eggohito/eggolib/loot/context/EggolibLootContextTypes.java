package io.github.eggohito.eggolib.loot.context;

import com.google.common.collect.BiMap;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.mixin.LootContextTypesAccessor;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.util.Identifier;

public class EggolibLootContextTypes {

	public static final LootContextType ANY = register(
		Eggolib.identifier("any"),
		LootContextType.create()
			.allow(LootContextParameters.THIS_ENTITY)
			.allow(LootContextParameters.LAST_DAMAGE_PLAYER)
			.allow(LootContextParameters.DAMAGE_SOURCE)
			.allow(LootContextParameters.KILLER_ENTITY)
			.allow(LootContextParameters.DIRECT_KILLER_ENTITY)
			.allow(LootContextParameters.ORIGIN)
			.allow(LootContextParameters.BLOCK_STATE)
			.allow(LootContextParameters.BLOCK_ENTITY)
			.allow(LootContextParameters.TOOL)
			.allow(LootContextParameters.EXPLOSION_RADIUS)
	);

	private static LootContextType register(Identifier id, LootContextType.Builder builder) {

		LootContextType lootContextType = builder.build();
		BiMap<Identifier, LootContextType> map = LootContextTypesAccessor.getMap();

		if (map.containsKey(id)) {
			throw new IllegalStateException("Loot table parameter \"" + id + "\" is already registered!");
		}

		map.put(id, lootContextType);
		return lootContextType;

	}

}
