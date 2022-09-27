package rbasamoyai.createbigcannons.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import rbasamoyai.createbigcannons.datagen.loot.CBCLootTableProvider;
import rbasamoyai.createbigcannons.datagen.recipes.*;
import rbasamoyai.createbigcannons.ponder.CBCPonderIndex;
import rbasamoyai.createbigcannons.ponder.CBCPonderTags;

public class CBCDatagen implements DataGeneratorEntrypoint {
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator gen) {

		BlockRecipeProvider.registerAll(gen);
		gen.addProvider(new CBCCraftingRecipeProvider(gen));
		gen.addProvider(new CBCCompactingRecipeProvider(gen));
		gen.addProvider(new MeltingRecipeProvider(gen));
		gen.addProvider(new CBCMixingRecipeProvider(gen));
		gen.addProvider(new CBCLootTableProvider(gen));

		CBCLangGen.prepare();

		CBCPonderTags.register();
		CBCPonderIndex.register();
		CBCPonderIndex.registerLang();
	}
	
}
