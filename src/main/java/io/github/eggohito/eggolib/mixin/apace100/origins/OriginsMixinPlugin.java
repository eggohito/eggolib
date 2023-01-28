package io.github.eggohito.eggolib.mixin.apace100.origins;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class OriginsMixinPlugin implements IMixinConfigPlugin {

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {

        boolean[] shouldApply = {false};
        FabricLoader.getInstance().getModContainer("origins").ifPresent(
            origins -> {

                String version = origins.getMetadata().getVersion().getFriendlyString();

                if (version.contains("+")) version = version.split("\\+")[0];
                if (version.contains("-")) version = version.split("-")[0];

                String[] splitVersion = version.split("\\.");
                int[] semanticVersion = new int[splitVersion.length];

                for (int i = 0; i < semanticVersion.length; i++) {
                    semanticVersion[i] = Integer.parseInt(splitVersion[i]);
                }

                shouldApply[0] = semanticVersion[0] <= 1 && semanticVersion[1] <= 6;

            }
        );

        return shouldApply[0];

    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

}
