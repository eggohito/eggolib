package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.data.EggolibDataTypes;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;
import org.apache.commons.lang3.tuple.Triple;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GameEventListenerPower extends CooldownPower implements VibrationListener.Callback {

    private final Consumer<Triple<World, BlockPos, Direction>> blockAction;
    private final Consumer<Pair<Entity, Entity>> biEntityAction;
    private final Predicate<CachedBlockPosition> blockCondition;
    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    
    private final EntityGameEventHandler<VibrationListener> gameEventHandler;
    private final int range;

    private final List<GameEvent> acceptedGameEvents;
    private final TagKey<GameEvent> acceptedGameEventTag;

    public GameEventListenerPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Triple<World, BlockPos, Direction>> blockAction, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<CachedBlockPosition> blockCondition, Predicate<Pair<Entity, Entity>> biEntityCondition, int cooldown, HudRender hudRender, int range, GameEvent gameEvent, List<GameEvent> gameEvents, TagKey<GameEvent> gameEventTag) {

        super(powerType, livingEntity, cooldown, hudRender);

        this.blockAction = blockAction;
        this.biEntityAction = biEntityAction;
        this.blockCondition = blockCondition;
        this.biEntityCondition = biEntityCondition;

        this.gameEventHandler = new EntityGameEventHandler<>(null);
        this.range = range;

        this.acceptedGameEvents = new ArrayList<>();
        if (gameEvent != null) this.acceptedGameEvents.add(gameEvent);
        if (gameEvents != null) this.acceptedGameEvents.addAll(gameEvents);

        this.acceptedGameEventTag = gameEventTag;
        this.setTicking();

    }

    public boolean canListen() {
        return gameEventHandler != null && gameEventHandler.getListener() != null;
    }

    public EntityGameEventHandler<VibrationListener> getGameEventHandler() {
        return gameEventHandler;
    }

    private EntityPositionSource getNewPositionSource() {
        return new EntityPositionSource(this.entity, this.entity.getEyeHeight(this.entity.getPose()));
    }

    @Override
    public void onAdded() {
        this.gameEventHandler.setListener(new VibrationListener(getNewPositionSource(), range, this), this.entity.world);
        this.entity.updateEventHandler(EntityGameEventHandler::onEntitySetPos);
    }

    @Override
    public void onRemoved() {
        this.entity.updateEventHandler(EntityGameEventHandler::onEntityRemoval);
    }

    @Override
    public void tick() {

        if (!(canListen() && canUse())) return;

        this.gameEventHandler.getListener().positionSource = getNewPositionSource();
        this.gameEventHandler.getListener().tick(this.entity.world);

    }

    @Override
    public TagKey<GameEvent> getTag() {
        return this.acceptedGameEventTag != null ? this.acceptedGameEventTag : VibrationListener.Callback.super.getTag();
    }

    @Override
    public boolean canAccept(GameEvent gameEvent, GameEvent.Emitter emitter) {
        return this.canUse() && (acceptedGameEvents.isEmpty() || acceptedGameEvents.contains(gameEvent)) && VibrationListener.Callback.super.canAccept(gameEvent, emitter);
    }

    @Override
    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
        
        if (this.entity.world != world) return false;
        Entity actor = emitter.sourceEntity();

        if (this.entity.equals(actor)) return false;
        return (blockCondition == null || blockCondition.test(new CachedBlockPosition(world, pos, true))) ||
               (biEntityCondition == null || biEntityCondition.test(new Pair<>(actor, this.entity)));

    }

    @Override
    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {

        this.use();

        if (blockAction != null) blockAction.accept(Triple.of(world, pos, Direction.UP));
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(entity, this.entity));

    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("game_event_listener"),
            new SerializableData()
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("cooldown", EggolibDataTypes.POSITIVE_INT, 1)
                .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
                .add("range", EggolibDataTypes.POSITIVE_INT, 16)
                .add("event", SerializableDataTypes.GAME_EVENT, null)
                .add("events", SerializableDataTypes.GAME_EVENTS, null)
                .add("tag", SerializableDataTypes.GAME_EVENT_TAG, null),
            data -> (powerType, livingEntity) -> new GameEventListenerPower(
                powerType,
                livingEntity,
                data.get("block_action"),
                data.get("bientity_action"),
                data.get("block_condition"),
                data.get("bientity_condition"),
                data.get("cooldown"),
                data.get("hud_render"),
                data.get("range"),
                data.get("event"),
                data.get("events"),
                data.get("tag")
            )
        ).allowCondition();
    }

}
