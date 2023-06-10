package io.github.eggohito.eggolib.mixin.apace100.apoli;

import io.github.apace100.apoli.util.InventoryUtil;
import io.github.eggohito.eggolib.util.StackReferenceUtil;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.StackReference;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryUtil.class)
public abstract class InventoryUtilMixin {

	@Redirect(method = {"lambda$replaceInventory$5", "lambda$modifyInventory$2", "lambda$dropInventory$8"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getStackReference(I)Lnet/minecraft/inventory/StackReference;"))
	private static StackReference eggolib$replaceStackReference(Entity instance, int mappedIndex) {
		return StackReferenceUtil.of(instance, mappedIndex);
	}

}
