package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.item.CopyToStorageAction;
import io.github.eggohito.eggolib.action.item.ExecuteCommandAction;
import io.github.eggohito.eggolib.action.item.ModifyItemCooldownAction;
import io.github.eggohito.eggolib.action.meta.LoopAction;
import net.minecraft.inventory.StackReference;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;
import net.minecraft.world.World;

public class EggolibItemActions {

	public static void register() {
		register(LoopAction.getFactory(ApoliDataTypes.ITEM_ACTION));
		register(CopyToStorageAction.getFactory());
		register(ExecuteCommandAction.getFactory());
		register(ModifyItemCooldownAction.getFactory());
	}

	public static ActionFactory<Pair<World, StackReference>> register(ActionFactory<Pair<World, StackReference>> actionFactory) {
		return Registry.register(ApoliRegistries.ITEM_ACTION, actionFactory.getSerializerId(), actionFactory);
	}

}
