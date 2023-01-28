package io.github.eggohito.eggolib.mixin.micalobia.breathinglib;

import dev.micalobia.breathinglib.data.BreathingInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BreathingInfo.Builder.class)
public interface BreathingInfoBuilderAccessor {

    @Accessor(remap = false)
    int getAirPerCycle();

    @Accessor(remap = false)
    float getDamagePerCycle();

}
