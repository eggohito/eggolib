package io.github.eggohito.eggolib.registry.factory;

import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.eggohito.eggolib.action.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public class EggolibEntityActions {

    public static void register() {
        register(CalculateResourceAction.getFactory());
        register(ChangeInsomniaTicksAction.getFactory());
        register(CloseGuiAction.getFactory());
        register(DropItemAction.getFactory());
        register(ModifyInventoryAction.getFactory());
        register(OpenGuiAction.getFactory());
        register(ReplaceItemAction.getFactory());
    }

    private static void register(ActionFactory<Entity> actionFactory) {
        Registry.register(ApoliRegistries.ENTITY_ACTION, actionFactory.getSerializerId(), actionFactory);
    }
}
