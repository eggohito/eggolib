package io.github.eggohito.eggolib.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.entity.GroupComponent;
import io.github.eggohito.eggolib.component.entity.GroupComponentImpl;
import net.minecraft.entity.Entity;

public class EggolibEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<GroupComponent> GROUP = ComponentRegistry.getOrCreate(Eggolib.identifier("groups"), GroupComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry
            .beginRegistration(Entity.class, GROUP)
            .impl(GroupComponentImpl.class)
            .respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY)
            .end(GroupComponentImpl::new);
    }

}
