package rbasamoyai.createbigcannons.crafting;

import java.util.function.Supplier;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeBuilder.ProcessingRecipeFactory;
import com.simibubi.create.content.contraptions.processing.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.recipe.IRecipeTypeInfo;

import io.github.fabricators_of_create.porting_lib.util.LazyRegistrar;
import io.github.fabricators_of_create.porting_lib.util.RegistryObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.crafting.foundry.MeltingRecipe;

public enum CBCRecipeTypes implements IRecipeTypeInfo {
	MELTING(MeltingRecipe::new);

	private final ResourceLocation id;
	private final RegistryObject<RecipeSerializer<?>> serializer;
	@Nullable
	private final RegistryObject<RecipeType<?>> typeObj;
	private final Supplier<RecipeType<?>> type;
	
	CBCRecipeTypes(Supplier<RecipeSerializer<?>> serializer) {
		String name = Lang.asId(this.name());
		this.id = CreateBigCannons.resource(name);
		this.serializer = Registries.SERIALIZERS.register(name, serializer);
		this.typeObj = Registries.RECIPE_TYPES.register(name, () -> AllRecipeTypes.simpleType(this.id));
		this.type = this.typeObj;
	}
	
	CBCRecipeTypes(ProcessingRecipeFactory<?> factory) {
		this(() -> new ProcessingRecipeSerializer<>(factory));
	}
	
	@Override public ResourceLocation getId() { return this.id; }
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) this.serializer.get();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) this.type.get();
	}
	
	public static void register() {
		Registries.SERIALIZERS.register();
		Registries.RECIPE_TYPES.register();
	}
	
	private static class Registries {
		private static final LazyRegistrar<RecipeSerializer<?>> SERIALIZERS = LazyRegistrar.create(Registry.RECIPE_SERIALIZER, CreateBigCannons.MOD_ID);
		private static final LazyRegistrar<RecipeType<?>> RECIPE_TYPES = LazyRegistrar.create(Registry.RECIPE_TYPE, CreateBigCannons.MOD_ID);
	}

}
