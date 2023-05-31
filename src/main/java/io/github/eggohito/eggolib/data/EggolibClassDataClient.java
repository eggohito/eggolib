package io.github.eggohito.eggolib.data;

import io.github.apace100.calio.ClassUtil;
import io.github.apace100.calio.data.ClassDataRegistry;
import io.github.eggohito.eggolib.Eggolib;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.screen.ingame.*;

public class EggolibClassDataClient {

	public static void register() {

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
		inGameScreen.addMapping("death", DeathScreen.class);
		inGameScreen.addMapping("enchantment", EnchantmentScreen.class);
		inGameScreen.addMapping("furnace", FurnaceScreen.class);
		inGameScreen.addMapping("game_menu", GameMenuScreen.class);
		inGameScreen.addMapping("game_mode_selection", GameModeSelectionScreen.class);
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

		Eggolib.LOGGER.info("[{}] Client class data registry has been successfully registered!", Eggolib.MOD_ID);
	}

}
