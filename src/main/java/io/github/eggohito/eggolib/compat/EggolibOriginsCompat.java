package io.github.eggohito.eggolib.compat;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.apace100.origins.screen.ChooseOriginScreen;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;

public class EggolibOriginsCompat extends EggolibModCompat {

    public static void init(ModContainer origins) {

        ClassDataRegistry<Screen> inGameScreen = ClassDataRegistry.getOrCreate(ClassUtil.castClass(Screen.class), "Screen");

        inGameScreen.addMapping("view_origin", ViewOriginScreen.class);
        inGameScreen.addMapping("choose_origin", ChooseOriginScreen.class);

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
