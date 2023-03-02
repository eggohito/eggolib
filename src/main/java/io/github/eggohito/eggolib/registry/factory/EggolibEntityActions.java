package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.entity.*;
import io.github.eggohito.eggolib.action.meta.LoopAction;
import net.minecraft.entity.Entity;
import net.minecraft.registry.Registry;

public class EggolibEntityActions {

    public static void register() {
        register(CalculateResourceAction.getFactory());
        register(ChangeHealthAction.getFactory());
        register(ChangeInsomniaTicksAction.getFactory());
        register(CloseScreenAction.getFactory());
        register(EggolibDamageAction.getFactory());
        register(EggolibDropInventoryAction.getFactory());
        register(EggolibReplaceInventoryAction.getFactory());
        register(FireProjectileAction.getFactory());
        register(LoopAction.getFactory(ApoliDataTypes.ENTITY_ACTION));
        register(ModifyHealthAction.getFactory());
        register(ModifyKeySequenceAction.getFactory());
        register(OpenInventoryAction.getFactory());
        register(PlaySoundAction.getFactory());
        register(RemovePowerAction.getFactory());
        register(SetPerspectiveAction.getFactory());
        register(SpawnEntityAction.getFactory());
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
