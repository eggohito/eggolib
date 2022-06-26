package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.meta.LoopAction;
import net.minecraft.entity.Entity;
import net.minecraft.util.Pair;
import net.minecraft.util.registry.Registry;

public class EggolibBiEntityActions {

    public static void register() {
        register(LoopAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
    }

    private static void register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

}
