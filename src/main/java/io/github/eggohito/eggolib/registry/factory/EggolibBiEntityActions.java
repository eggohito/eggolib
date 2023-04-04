package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.bientity.EggolibDamageAction;
import io.github.eggohito.eggolib.action.bientity.LeashAction;
import io.github.eggohito.eggolib.action.meta.LoopAction;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;
import net.minecraft.util.Pair;

public class EggolibBiEntityActions {

    public static void register() {
        register(EggolibDamageAction.getFactory());
        register(LeashAction.getFactory());
        register(LoopAction.getFactory(ApoliDataTypes.BIENTITY_ACTION));
    }

    public static ActionFactory<Pair<Entity, Entity>> register(ActionFactory<Pair<Entity, Entity>> actionFactory) {
        return Registry.register(ApoliRegistries.BIENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }

}
