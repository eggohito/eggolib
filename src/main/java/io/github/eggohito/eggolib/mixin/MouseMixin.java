package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.ModifyMouseSensitivityPower;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Mouse.class)
@Environment(EnvType.CLIENT)
public abstract class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    @ModifyVariable(method = "updateMouse", at = @At("STORE"), ordinal = 2)
    private double eggolib$modifyMouseSensitivity(double originalValue) {

        if (this.client.player == null) return originalValue;
        double newValue = PowerHolderComponent.modify(this.client.player, ModifyMouseSensitivityPower.class, originalValue);

        if (newValue != originalValue) return newValue;
        else return originalValue;

    }

}
