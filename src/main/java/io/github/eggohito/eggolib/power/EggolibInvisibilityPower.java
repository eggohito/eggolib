package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Pair;

import java.util.function.Predicate;

public class EggolibInvisibilityPower extends Power {

    private final Predicate<Pair<Entity, Entity>> biEntityCondition;
    private final boolean renderArmor;

    public EggolibInvisibilityPower(PowerType<?> powerType, LivingEntity livingEntity, Predicate<Pair<Entity, Entity>> biEntityCondition, boolean renderArmor) {
        super(powerType, livingEntity);
        this.biEntityCondition = biEntityCondition;
        this.renderArmor = renderArmor;
    }

    public boolean doesApply(PlayerEntity viewer) {
        return biEntityCondition == null || biEntityCondition.test(new Pair<>(viewer, entity));
    }

    public boolean shouldRenderArmor() {
        return renderArmor;
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("invisibility"),
            new SerializableData()
                .add("bientity_condition", ApoliDataTypes.BIENTITY_CONDITION, null)
                .add("render_armor", SerializableDataTypes.BOOLEAN),
            data -> (powerType, livingEntity) -> new EggolibInvisibilityPower(
                powerType,
                livingEntity,
                data.get("bientity_condition"),
                data.getBoolean("render_armor")
            )
        ).allowCondition();
    }

}
