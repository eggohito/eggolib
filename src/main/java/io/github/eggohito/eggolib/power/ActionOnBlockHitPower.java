package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import java.util.EnumSet;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ActionOnBlockHitPower extends Power {

    private final Consumer<Entity> entityAction;
    private final Consumer<Entity> projectileAction;
    private final Consumer<Triple<World, BlockPos, Direction>> blockAction;
    private final Predicate<Entity> projectileCondition;
    private final Predicate<CachedBlockPosition> blockCondition;
    private final EnumSet<Direction> directions;

    public ActionOnBlockHitPower(PowerType<?> powerType, LivingEntity livingEntity, Consumer<Entity> entityAction, Consumer<Entity> projectileAction, Consumer<Triple<World, BlockPos, Direction>> blockAction, Predicate<Entity> projectileCondition, Predicate<CachedBlockPosition> blockCondition, EnumSet<Direction> directions) {
        super(powerType, livingEntity);
        this.entityAction = entityAction;
        this.projectileAction = projectileAction;
        this.blockAction = blockAction;
        this.projectileCondition = projectileCondition;
        this.blockCondition = blockCondition;
        this.directions = directions;
    }

    public boolean doesApply(World world, BlockPos blockPos, Direction direction, ProjectileEntity projectileEntity) {

        if (!directions.contains(direction)) return false;
        return projectileEntity == null ?
               projectileCondition == null && (blockCondition == null || blockCondition.test(new CachedBlockPosition(world, blockPos, true))) :
               (projectileCondition != null && projectileCondition.test(projectileEntity)) && (blockCondition == null || blockCondition.test(new CachedBlockPosition(world, blockPos, true)));

    }

    public void executeActions(World world, BlockPos blockPos, Direction direction, ProjectileEntity projectileEntity) {
        if (blockAction != null) blockAction.accept(Triple.of(world, blockPos, direction));
        if (projectileEntity != null && projectileAction != null) projectileAction.accept(projectileEntity);
        if (entityAction != null) entityAction.accept(entity);
    }

    public static ActionResult integrateCallback(PlayerEntity playerEntity, World world, Hand hand, BlockPos blockPos, Direction direction) {

        if (!playerEntity.isSpectator()) PowerHolderComponent.withPowers(
            playerEntity,
            ActionOnBlockHitPower.class,
            aobhp -> aobhp.doesApply(world, blockPos, direction, null),
            aobhp -> aobhp.executeActions(world, blockPos, direction, null)
        );

        return ActionResult.PASS;

    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("action_on_block_hit"),
            new SerializableData()
                .add("entity_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("projectile_action", ApoliDataTypes.ENTITY_ACTION, null)
                .add("block_action", ApoliDataTypes.BLOCK_ACTION, null)
                .add("projectile_condition", ApoliDataTypes.ENTITY_CONDITION, null)
                .add("block_condition", ApoliDataTypes.BLOCK_CONDITION, null)
                .add("directions", SerializableDataTypes.DIRECTION_SET, EnumSet.allOf(Direction.class)),
            data -> (powerType, livingEntity) -> new ActionOnBlockHitPower(
                powerType,
                livingEntity,
                data.get("entity_action"),
                data.get("projectile_action"),
                data.get("block_action"),
                data.get("projectile_condition"),
                data.get("block_condition"),
                data.get("directions")
            )
        ).allowCondition();
    }

}
