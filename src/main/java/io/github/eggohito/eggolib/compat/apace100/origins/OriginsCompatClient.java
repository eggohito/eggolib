package io.github.eggohito.eggolib.compat.apace100.origins;

import io.github.apace100.origins.screen.ChooseOriginScreen;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.eggohito.eggolib.Eggolib;
import io.github.eggohito.eggolib.compat.EggolibModCompatClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.ModContainer;

@Environment(EnvType.CLIENT)
public class OriginsCompatClient extends EggolibModCompatClient {

    @Override
    public void init() {

    }

    @Override
    public void initOn(ModContainer origins) {

        addScreenMapping("choose_origin", ChooseOriginScreen.class);
        addScreenMapping("view_origin", ViewOriginScreen.class);

        Eggolib.LOGGER.warn(
            String.format(
                "[%1$s] Detected '%2$s' by %3$s! Successfully registered these screen classes to %1$s's class data registry:\n%4$s",
                Eggolib.MOD_ID,
                getModName(origins),
                getModAuthors(origins),
                formatScreenMappings()
            )
        );

    }

    @Override
    public String getCompatTarget() {
        return "origins";
    }

}
