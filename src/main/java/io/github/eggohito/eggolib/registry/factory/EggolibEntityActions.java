package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public class EggolibEntityActions {

    public static void register() {
        register(CalculateResourceAction.getFactory());
        register(ChangeHealthAction.getFactory());
        register(ChangeInsomniaTicksAction.getFactory());
        register(CloseScreenAction.getFactory());
        register(DropItemAction.getFactory());
        register(LoopAction.getFactory());
        register(ModifyInventoryAction.getFactory());
        register(OpenScreenAction.getFactory());
        register(ReplaceItemAction.getFactory());
        register(SetPerspectiveAction.getFactory());
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
