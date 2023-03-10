package io.github.eggohito.eggolib.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;

public class CrawlingPower extends Power {

    public CrawlingPower(PowerType<?> powerType, LivingEntity livingEntity) {
        super(powerType, livingEntity);
        this.setTicking();
    }

    @Override
    public void tick() {
        if (!entity.isCrawling()) entity.setPose(EntityPose.SWIMMING);
    }

    public static PowerFactory<?> getFactory() {
        return new PowerFactory<>(
            Eggolib.identifier("crawling"),
            new SerializableData(),
            data -> (powerType, livingEntity) -> new CrawlingPower(
                powerType,
                livingEntity
            )
        ).allowCondition();
    }

}
