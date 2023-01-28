package io.github.eggohito.eggolib.mixin.apace100.calio;

import io.github.apace100.calio.data.ClassDataRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashMap;

@Mixin(ClassDataRegistry.class)
public interface ClassDataRegistryAccessor {

    @Accessor(value = "directMappings", remap = false)
    HashMap<String, Class<?>> getMappings();

}
