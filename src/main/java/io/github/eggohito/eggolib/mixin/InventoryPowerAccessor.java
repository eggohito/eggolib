package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.power.InventoryPower;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(InventoryPower.class)
public interface InventoryPowerAccessor {

    @Accessor
    TranslatableText getContainerName();

    @Accessor
    ScreenHandlerFactory getFactory();
}
