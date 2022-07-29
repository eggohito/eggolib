package io.github.eggohito.eggolib.registry.factory;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import io.github.eggohito.eggolib.component.entity.*;
import net.minecraft.entity.Entity;

public class EggolibEntityComponents implements EntityComponentInitializer {

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {

        registry
            .beginRegistration(Entity.class, MiscComponent.KEY)
            .impl(MiscComponentImpl.class)
            .respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY)
            .end(MiscComponentImpl::new);

    }

}
