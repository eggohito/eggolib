package io.github.eggohito.eggolib.compat;

import io.github.apace100.origins.screen.ChooseOriginScreen;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.loader.api.ModContainer;

public class EggolibOriginsCompat extends EggolibModCompat {

    public static void init(ModContainer origins) {

        addScreenMapping("view_origin", ViewOriginScreen.class);
        addScreenMapping("choose_origin", ChooseOriginScreen.class);

        Eggolib.LOGGER.warn(
            String.format(
                "[%1$s] Detected '%2$s' by %3$s! Successfully added its screen classes to %1$s's screen class data registry",
                Eggolib.MOD_ID,
                getModName(origins),
                getModAuthors(origins)
            )
        );

    }

}
