package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.ModifyLabelRenderPower;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.Optional;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {

	@Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
	private void eggolib$completelyHideLabel(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		if (PowerHolderComponent.hasPower(entity, ModifyLabelRenderPower.class, mlrp -> mlrp.getMode() == ModifyLabelRenderPower.RenderMode.HIDE_COMPLETELY)) {
			ci.cancel();
		}
	}

	@ModifyVariable(method = "renderLabelIfPresent", at = @At(value = "HEAD"), argsOnly = true)
	private Text eggolib$replaceLabelText(Text originalValue, T entity) {

		Optional<ModifyLabelRenderPower> modifyLabelRenderPower = PowerHolderComponent
			.getPowers(entity, ModifyLabelRenderPower.class)
			.stream()
			.max(Comparator.comparing(ModifyLabelRenderPower::getPriority));

		return modifyLabelRenderPower.map(ModifyLabelRenderPower::getReplacementText).orElse(originalValue);

	}

	@ModifyVariable(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"), ordinal = 0)
	private boolean eggolib$partiallyHideLabel(boolean originalValue, T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
		if (PowerHolderComponent.hasPower(entity, ModifyLabelRenderPower.class, mlrp -> mlrp.getMode() == ModifyLabelRenderPower.RenderMode.HIDE_PARTIALLY)) {
			return false;
		} else {
			return originalValue;
		}
	}

}
