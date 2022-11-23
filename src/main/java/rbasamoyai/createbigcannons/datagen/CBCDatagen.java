package rbasamoyai.createbigcannons.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.datagen.loot.CBCLootTableProvider;
import rbasamoyai.createbigcannons.datagen.recipes.BlockRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.CBCCompactingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.CBCCraftingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.CBCMixingRecipeProvider;
import rbasamoyai.createbigcannons.datagen.recipes.MeltingRecipeProvider;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.ponder.CBCPonderTags;

@Mod.EventBusSubscriber(modid = CreateBigCannons.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CBCDatagen {
	
	@SubscribeEvent
	public static void onDatagen(GatherDataEvent event) {
		DataGenerator gen = event.getGenerator();
		if (event.includeServer()) {
			BlockRecipeProvider.registerAll(gen);
			gen.addProvider(true, new CBCCraftingRecipeProvider(gen));
			gen.addProvider(true, new CBCCompactingRecipeProvider(gen));
			gen.addProvider(true, new MeltingRecipeProvider(gen));
			gen.addProvider(true, new CBCMixingRecipeProvider(gen));
			gen.addProvider(true, new CBCLootTableProvider(gen));
		}
		if (event.includeClient()) {
			CBCLangGen.prepare();
			
			CBCPonderTags.register();
			CBCPonderIndex.register();
			CBCPonderIndex.registerLang();
		}
	}
	
}
