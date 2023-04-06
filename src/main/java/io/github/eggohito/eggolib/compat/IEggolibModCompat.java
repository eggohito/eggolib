package io.github.eggohito.eggolib.compat;

import net.fabricmc.loader.api.ModContainer;

public interface IEggolibModCompat {

    /**
     *  A method that will be called upon initializing eggolib.
     */
    void init();

    /**
     *  A method that will be called upon initializing eggolib and if the compatibility mod target
     *  (defined by {@link IEggolibModCompat#getCompatTarget()}) is loaded. (mostly used by eggolib internally)
     *  @param modContainer     the {@link ModContainer} to target for compatibility
     */
    default void initOn(ModContainer modContainer) {

    }

    /**
     *  Returns the namespace of the mod targeted for compatibility.
     *  (mostly used by eggolib internally)
     *  @return     the namespace of the mod to target for compatibility
     */
    default String getCompatTarget() {
        return null;
    }

}
