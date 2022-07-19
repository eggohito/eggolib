package io.github.eggohito.eggolib.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.eggohito.eggolib.power.EggolibInvisibilityPower;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    @Unique
    private final List<FeatureRenderer<?, ?>> eggolib$cachedFeatureRenderers = new ArrayList<>();

    @Unique
    private boolean eggolib$replacedFeatureRenderers = false;

    @Inject(method = "addFeature", at = @At("TAIL"))
    private void eggolib$cacheNewFeatureRenderer(FeatureRenderer<?, ?> feature, CallbackInfoReturnable<Boolean> cir) {
        this.eggolib$cachedFeatureRenderers.add(feature);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Ljava/util/List;iterator()Ljava/util/Iterator;"))
    private void eggolib$preventArmorRender(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {

        LivingEntityRenderer<?, ?> thisAsLivingEntityRenderer = (LivingEntityRenderer<?, ?>) (Object) this;
        List<FeatureRenderer<?, ?>> originalFeatureRenderers = ((LivingEntityRendererAccessor) thisAsLivingEntityRenderer).getFeatures();

        if (PowerHolderComponent.hasPower(livingEntity, EggolibInvisibilityPower.class, eip -> !eip.shouldRenderArmor())) {
            this.eggolib$replacedFeatureRenderers = true;
            originalFeatureRenderers.removeIf(featureRenderer -> featureRenderer instanceof ArmorFeatureRenderer<?,?,?>);
        }

        else if (this.eggolib$replacedFeatureRenderers) {
            this.eggolib$replacedFeatureRenderers = false;
            originalFeatureRenderers.clear();
            originalFeatureRenderers.addAll(this.eggolib$cachedFeatureRenderers);
        }

    }

}
