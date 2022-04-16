package io.github.eggohito.eggolib.data;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.apace100.origins.screen.ChooseOriginScreen;
import io.github.apace100.origins.screen.ViewOriginScreen;
import io.github.eggohito.eggolib.Eggolib;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.ingame.*;

import java.util.Collection;
import java.util.Optional;

public class EggolibClassDataClient {

    public static void registerAll() {

        FabricLoader fabricLoader = FabricLoader.getInstance();
        ClassDataRegistry<Screen> inGameScreen = ClassDataRegistry.getOrCreate(ClassUtil.castClass(Screen.class), "Screen");

        inGameScreen.addMapping("anvil", AnvilScreen.class);
        inGameScreen.addMapping("beacon", BeaconScreen.class);
        inGameScreen.addMapping("blast_furnace", BlastFurnaceScreen.class);
        inGameScreen.addMapping("book", BookScreen.class);
        inGameScreen.addMapping("book_edit", BookEditScreen.class);
        inGameScreen.addMapping("brewing_stand", BrewingStandScreen.class);
        inGameScreen.addMapping("cartography_table", CartographyTableScreen.class);
        inGameScreen.addMapping("chat", ChatScreen.class);
        inGameScreen.addMapping("command_block", CommandBlockScreen.class);
        inGameScreen.addMapping("crafting", CraftingScreen.class);
        inGameScreen.addMapping("creative_inventory", CreativeInventoryScreen.class);
        inGameScreen.addMapping("enchantment", EnchantmentScreen.class);
        inGameScreen.addMapping("furnace", FurnaceScreen.class);
        inGameScreen.addMapping("generic_3x3_container", Generic3x3ContainerScreen.class);
        inGameScreen.addMapping("generic_container", GenericContainerScreen.class);
        inGameScreen.addMapping("grindstone", GrindstoneScreen.class);
        inGameScreen.addMapping("hopper", HopperScreen.class);
        inGameScreen.addMapping("horse", HorseScreen.class);
        inGameScreen.addMapping("inventory", InventoryScreen.class);
        inGameScreen.addMapping("jigsaw_block", JigsawBlockScreen.class);
        inGameScreen.addMapping("lectern", LecternScreen.class);
        inGameScreen.addMapping("loom", LoomScreen.class);
        inGameScreen.addMapping("merchant", MerchantScreen.class);
        inGameScreen.addMapping("minecart_command_block", MinecartCommandBlockScreen.class);
        inGameScreen.addMapping("shulker_box", ShulkerBoxScreen.class);
        inGameScreen.addMapping("sign_edit", SignEditScreen.class);
        inGameScreen.addMapping("smithing", SmithingScreen.class);
        inGameScreen.addMapping("smoker", SmokerScreen.class);
        inGameScreen.addMapping("stonecutter", StonecutterScreen.class);
        inGameScreen.addMapping("structure_block", StructureBlockScreen.class);

        // Additional classes to add to the `inGameScreen` class data registry
        Optional<ModContainer> origins = fabricLoader.getModContainer("origins");
        if (origins.isPresent()) {

            displayModInfo(origins.get());

            inGameScreen.addMapping("view_origin", ViewOriginScreen.class);
            inGameScreen.addMapping("choose_origin", ChooseOriginScreen.class);
        }

        Eggolib.LOGGER.info("[eggolib] Client class data registry has been successfully registered!");
    }

    private static void displayModInfo(ModContainer modContainer) {

        ModMetadata modMetadata = modContainer.getMetadata();

        String modName = modMetadata.getName();
        StringBuilder stringBuilder = new StringBuilder();
        Collection<Person> persons = modMetadata.getAuthors();

        String separator = "";
        for (Person person : persons) {
            stringBuilder
                .append(separator)
                .append(person.getName());
            separator = ", ";
        }
        String modAuthors = stringBuilder.toString();

        Eggolib.LOGGER.warn(
            "[eggolib] Detected '{}' by {}! Adding some of its classes to eggolib's class data registry...",
            modName,
            modAuthors
        );
    }
}
