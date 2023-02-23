package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.SavedBlockPosition;
import io.github.apace100.apoli.util.modifier.Modifier;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ModifyBouncinessPower extends ValueModifyingPower {

    private final Consumer<Entity> entityAction;
    private final Consumer<Triple<World, BlockPos, Direction>> blockAction;
    private final Predicate<CachedBlockPosition> blockCondition;

    private SavedBlockPosition landedOnBlockCache;

    public ModifyBouncinessPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Entity> entityAction, Consumer<Triple<World, BlockPos, Direction>> blockAction, Predicate<CachedBlockPosition> blockCondition, Modifier modifier, List<Modifier> modifiers) {
        super(powerType, livingEntity);
        this.entityAction = entityAction;
        this.blockAction = blockAction;
        this.blockCondition = blockCondition;
        if (modifier != null) addModifier(modifier);
        if (modifiers != null) modifiers.forEach(this::addModifier);
    }

    public boolean doesApply() {
        return landedOnBlockCache != null && (blockCondition == null || blockCondition.test(landedOnBlockCache));
    }

    public boolean doesApply(World world, BlockPos blockPos) {
        landedOnBlockCache = new SavedBlockPosition(world, blockPos);
        return doesApply();
    }

    public void executeActions() {
        if (blockAction != null && landedOnBlockCache != null) blockAction.accept(Triple.of(entity.world, landedOnBlockCache.getBlockPos(), Direction.UP));
        if (entityAction != null) entityAction.accept(entity);
        clearCache();
    }

    public void clearCache() {
        landedOnBlockCache = null;
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("modify_bounciness"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                .add("modifier", Modifier.DATA_TYPE, null)
                .add("modifiers", Modifier.LIST_TYPE, null),
            data -> (powerType, livingEntity) -> new ModifyBouncinessPower(
                powerType,
                livingEntity,
                data.get("entity_action"),
                data.get("block_action"),
                data.get("block_condition"),
                data.get("modifier"),
                data.get("modifiers")
            )
        ).allowCondition();
    }

}
