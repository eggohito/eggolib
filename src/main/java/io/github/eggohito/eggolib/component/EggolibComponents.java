package io.github.eggohito.eggolib.component;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.component.entity.IMiscComponent;

public interface EggolibComponents {

	ComponentKey<IMiscComponent> MISC = ComponentRegistry.getOrCreate(Eggolib.identifier("misc"), IMiscComponent.class);

}
