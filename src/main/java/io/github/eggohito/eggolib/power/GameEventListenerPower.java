package io.github.eggohito.eggolib.power;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.TagKey;
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

public class GameEventListenerPower extends Power implements VibrationListener.Callback {

    private final Consumer<Triple<World, BlockPos, Direction>> blockAction;
    private final Consumer<Pair<Entity, Entity>> biEntityAction;
    private final Predicate<CachedBlockPosition> blockCondition;
    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    
    public EntityGameEventHandler<VibrationListener> gameEventHandler;

    private final List<GameEvent> acceptedGameEvents;
    private final TagKey<GameEvent> acceptedGameEventTag;

    public GameEventListenerPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Triple<World, BlockPos, Direction>> blockAction, Consumer<Pair<Entity, Entity>> biEntityAction, Predicate<CachedBlockPosition> blockCondition, Predicate<Pair<Entity, Entity>> biEntityCondition, int range, GameEvent gameEvent, List<GameEvent> gameEvents, TagKey<GameEvent> gameEventTag) {
        super(powerType, livingEntity);
        this.blockAction = blockAction;
        this.biEntityAction = biEntityAction;
        this.blockCondition = blockCondition;
        this.biEntityCondition = biEntityCondition;
        this.gameEventHandler = new EntityGameEventHandler<>(
            new VibrationListener(
                getPositionSource(),
                range,
                this,
                null,
                0.0f,
                0
            )
        );
        this.acceptedGameEvents = new ArrayList<>();
        if (gameEvent != null) this.acceptedGameEvents.add(gameEvent);
        if (gameEvents != null) this.acceptedGameEvents.addAll(gameEvents);
        this.acceptedGameEventTag = gameEventTag;
        this.setTicking();
    }

    private EntityPositionSource getPositionSource() {
        return new EntityPositionSource(
            this.entity,
            this.entity.getEyeHeight(this.entity.getPose())
        );
    }

    @Override
    public void tick() {
        this.gameEventHandler.getListener().positionSource = getPositionSource();
        if (this.isActive()) this.gameEventHandler.getListener().tick(this.entity.world);
    }

    @Override
    public NbtElement toTag() {

        NbtCompound nbtCompound = new NbtCompound();
        DataResult<?> vibrationListenerDataResult = VibrationListener.createCodec(this).encodeStart(NbtOps.INSTANCE, this.gameEventHandler.getListener());
        
        vibrationListenerDataResult.resultOrPartial(LogUtils.getLogger()::error).ifPresent(
            o -> nbtCompound.put("listener", (NbtElement) o)
        );
        
        return nbtCompound;
        
    }

    @Override
    public void fromTag(NbtElement tag) {
        
        if (!(tag instanceof NbtCompound nbtCompound)) return;
        
        DataResult<?> vibrationListenerDataResult = VibrationListener.createCodec(this).parse(new Dynamic<>(NbtOps.INSTANCE, nbtCompound.getCompound("listener")));
        vibrationListenerDataResult.resultOrPartial(LogUtils.getLogger()::error).ifPresent(
            o -> this.gameEventHandler.setListener((VibrationListener) o, this.entity.world)
        );
        
    }

    @Override
    public boolean canAccept(GameEvent gameEvent, GameEvent.Emitter emitter) {
        return this.isActive() && VibrationListener.Callback.super.canAccept(gameEvent, emitter);
    }

    @Override
    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
        
        if (entity.world != world) return false;
        Entity target = emitter.sourceEntity();
        
        return
            this.isActive() &&
            (!entity.equals(target)) &&
            (acceptedGameEventTag == null || event.isIn(acceptedGameEventTag)) &&
            (acceptedGameEvents.isEmpty() || acceptedGameEvents.contains(event)) &&
            target == null ?
            (blockCondition == null || blockCondition.test(new CachedBlockPosition(world, pos, true))) && biEntityCondition == null :
            (blockCondition == null || blockCondition.test(new CachedBlockPosition(world, pos, true))) && (biEntityCondition == null || biEntityCondition.test(new Pair<>(this.entity, target)));
        
    }

    @Override
    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, float distance) {
        if (blockAction != null) blockAction.accept(Triple.of(world, pos, Direction.UP));
        if (biEntityAction != null) biEntityAction.accept(new Pair<>(this.entity, entity));
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("game_event_listener"),
            new SerializableData()
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("bientity_action", ApoliDataTypes.BIENTITY_ACTION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("range", SerializableDataTypes.INT, 16)
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
                data.getInt("range"),
                data.get("event"),
                data.get("events"),
                data.get("tag")
            )
        ).allowCondition();
    }

}
