package io.github.eggohito.eggolib.compat;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.apace100.origins.screen.ChooseOriginScreen;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Pair;

public class EggolibOriginsCompat {

    public static void init(ModContainer modContainer) {

        ClassDataRegistry<Screen> inGameScreen = ClassDataRegistry.getOrCreate(ClassUtil.castClass(Screen.class), "Screen");

        inGameScreen.addMapping("view_origin", ViewOriginScreen.class);
        inGameScreen.addMapping("choose_origin", ChooseOriginScreen.class);

        Pair<String, String> origins = Eggolib.getModCompatNameAndAuthors(modContainer);
        Eggolib.LOGGER.warn(
            "[{}] Detected '{}' by {}! Successfully added some of its classes to eggolib's class data registry",
            Eggolib.MOD_ID,
            origins.getLeft(),
            origins.getRight()
        );

    }

}
