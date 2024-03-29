package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.meta.LoopAction;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

public class EggolibBlockActions {

	public static void register() {
		register(LoopAction.getFactory(ApoliDataTypes.BLOCK_ACTION));
	}

	public static ActionFactory<Triple<World, BlockPos, Direction>> register(ActionFactory<Triple<World, BlockPos, Direction>> actionFactory) {
		return Registry.register(ApoliRegistries.BLOCK_ACTION, actionFactory.getSerializerId(), actionFactory);
	}

}
