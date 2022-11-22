package rbasamoyai.createbigcannons.base;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.BlockRecipeSerializer;
import rbasamoyai.createbigcannons.crafting.BlockRecipeType;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import java.util.function.Supplier;

public class CBCRegistries {

	public static final Supplier<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS =
			LazyRegistrar.create(Keys.BLOCK_RECIPE_SERIALIZERS, CreateBigCannons.MOD_ID).makeRegistry();

	public static final Supplier<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES =
			LazyRegistrar.create(Keys.BLOCK_RECIPE_TYPES, CreateBigCannons.MOD_ID).makeRegistry();

	public static final Supplier<Registry<CannonCastShape>> CANNON_CAST_SHAPES =
			LazyRegistrar.create(Keys.CANNON_CAST_SHAPES, CreateBigCannons.MOD_ID).makeRegistry();

	public static class Keys {
		public static final ResourceKey<Registry<BlockRecipeSerializer<?>>> BLOCK_RECIPE_SERIALIZERS = key("block_recipe_serializers");
		public static final ResourceKey<Registry<BlockRecipeType<?>>> BLOCK_RECIPE_TYPES = key("block_recipe_types");
		public static final ResourceKey<Registry<CannonCastShape>> CANNON_CAST_SHAPES = key("cannon_cast_shapes");

		private static <T> ResourceKey<Registry<T>> key(String id) { return ResourceKey.createRegistryKey(CreateBigCannons.resource(id)); }
	}
	
	public static void init() {
		BLOCK_RECIPE_SERIALIZERS.get();
		BLOCK_RECIPE_TYPES.get();
		CANNON_CAST_SHAPES.get();
	}
	
}
