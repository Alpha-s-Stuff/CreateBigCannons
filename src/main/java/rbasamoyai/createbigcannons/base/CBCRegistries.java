package rbasamoyai.createbigcannons.base;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;

public class CBCRegistries {

	public static final Registry<BlockRecipeSerializer> BLOCK_RECIPE_SERIALIZERS =
			FabricRegistryBuilder.createSimple(BlockRecipeSerializer.class, CreateBigCannons.resource("block_recipe_serializers")).buildAndRegister();
	
	public static class Keys {
		public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
		
		private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }
	}
	
	public static void init() {}
	
}
