package io.github.eggohito.eggolib.action.entity;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.factory.action.ActionFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.util.ArgumentWrapper;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SelectAction {

    public static void action(SerializableData.Instance data, Entity entity) {

        MinecraftServer server = entity.world.getServer();
        if (server == null) return;

        ArgumentWrapper<EntitySelector> wrappedSelector = data.get("selector");
        EntitySelector selector = wrappedSelector.get();
        Consumer<Pair<Entity, Entity>> biEntityAction = data.get("bientity_action");
        Predicate<Pair<Entity, Entity>> biEntityCondition = data.get("bientity_condition");

        ServerCommandSource source = new ServerCommandSource(
            CommandOutput.DUMMY,
            entity.getPos(),
            entity.getRotationClient(),
            (ServerWorld) entity.world,
            Apoli.config.executeCommand.permissionLevel,
            entity.getEntityName(),
            entity.getName(),
            server,
            entity
        );

        try {
            selector.getEntities(source).forEach(
                target -> {
                    if (!(biEntityCondition == null || biEntityCondition.test(new Pair<>(entity, target)))) return;
                    if (biEntityAction != null) biEntityAction.accept(new Pair<>(entity, target));
                }
            );
        }
        catch (CommandSyntaxException ignored) {}

    }

    public static ActionFactory<Entity> getFactory() {
        return new ActionFactory<>(
            Eggolib.identifier("select"),
            new SerializableData()
                .add("selector", EggolibDataTypes.ENTITIES_SELECTOR)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null),
            SelectAction::action
        );
    }

}
